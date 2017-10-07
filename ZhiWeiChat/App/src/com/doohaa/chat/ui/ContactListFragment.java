/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.doohaa.chat.ui;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.util.EMLog;
import com.doohaa.chat.IMHelper;
import com.doohaa.chat.IMHelper.DataSyncListener;
import com.doohaa.chat.R;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.ApiResponse;
import com.doohaa.chat.db.InviteMessgeDao;
import com.doohaa.chat.db.UserDao;
import com.doohaa.chat.ui.AddContact.AddContactActivity;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.widget.ContactItemView;

import java.util.Hashtable;
import java.util.Map;

/**
 * 联系人列表页
 */
public class ContactListFragment extends EaseContactListFragment {

    private static final String TAG = ContactListFragment.class.getSimpleName();
    private ContactSyncListener contactSyncListener;
    private BlackListSyncListener blackListSyncListener;
    private ContactInfoSyncListener contactInfoSyncListener;
    private View loadingView;
    private ContactItemView applicationItem;
    private InviteMessgeDao inviteMessgeDao;

    @Override
    protected void initView() {
        super.initView();
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, null);
        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
        applicationItem.setOnClickListener(clickListener);
//        headerView.findViewById(R.id.group_item).setOnClickListener(clickListener);
//        headerView.findViewById(R.id.chat_room_item).setOnClickListener(clickListener);
//        headerView.findViewById(R.id.robot_item).setOnClickListener(clickListener);
        //添加headerview
        listView.addHeaderView(headerView);
        //添加正在加载数据提示的loading view
        loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.em_layout_loading_data, null);
        contentContainer.addView(loadingView);
        //注册上下文菜单
        registerForContextMenu(listView);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void refresh() {
//        Response.Listener<FriendListDto> successListener = new Response.Listener<FriendListDto>() {
//            @Override
//            public void onResponse(FriendListDto response) {
//                if (Validator.isNotEmpty(response)) {
//                    ArrayList<UserDto> data = response.getData();
//                    if (getActivity() != null && !((BaseActivity) getActivity()).isFinishingDestroyed()) {
//
//                        Log.e("xxx", "刷新联系人");
//                        Map<String, EaseUser> m = IMHelper.getInstance().getContactList();
//                        if (m instanceof Hashtable<?, ?>) {
//                            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>) m).clone();
//                        }
//                        setContactsMap(m);
//                        ContactListFragment.super.refresh();
//                        if (inviteMessgeDao == null) {
//                            inviteMessgeDao = new InviteMessgeDao(getActivity());
//                        }
//                        if (inviteMessgeDao.getUnreadMessagesCount() > 0) {
//                            applicationItem.showUnreadMsgView();
//                        } else {
//                            applicationItem.hideUnreadMsgView();
//                        }
//                    }
//                }
//            }
//        };
//
//        Response.ErrorListener errorListener = new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (getActivity() != null && !((BaseActivity) getActivity()).isFinishingDestroyed()) {
//
//                }
//            }
//        };
//
//        ApiHelper.friendList(getActivity(), FriendListDto.class, successListener, errorListener);
        Map<String, EaseUser> m = IMHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>) m).clone();
        }
        setContactsMap(m);
        super.refresh();
        if (inviteMessgeDao == null) {
            inviteMessgeDao = new InviteMessgeDao(getActivity());
        }
        if (inviteMessgeDao.getUnreadMessagesCount() > 0) {
            applicationItem.showUnreadMsgView();
        } else {
            applicationItem.hideUnreadMsgView();
        }

    }


    @SuppressWarnings("unchecked")
    @Override
    protected void setUpView() {
        titleBar.setRightImageResource(R.drawable.em_add);
        titleBar.setRightLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddContactActivity.class));
            }
        });

        //设置联系人数据
        Map<String, EaseUser> m = IMHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>) m).clone();
        }
        setContactsMap(m);
        super.setUpView();
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = ((EaseUser) listView.getItemAtPosition(position)).getUsername();
                // demo中直接进入聊天页面，实际一般是进入用户详情页
                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", username));
            }
        });


        // 进入添加好友页
        titleBar.getRightLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddContactActivity.class));
            }
        });


        contactSyncListener = new ContactSyncListener();
        IMHelper.getInstance().addSyncContactListener(contactSyncListener);

        blackListSyncListener = new BlackListSyncListener();
        IMHelper.getInstance().addSyncBlackListListener(blackListSyncListener);

        contactInfoSyncListener = new ContactInfoSyncListener();
        IMHelper.getInstance().getUserProfileManager().addSyncContactInfoListener(contactInfoSyncListener);

        if (IMHelper.getInstance().isContactsSyncedWithServer()) {
            loadingView.setVisibility(View.GONE);
        } else if (IMHelper.getInstance().isSyncingContactsWithServer()) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (contactSyncListener != null) {
            IMHelper.getInstance().removeSyncContactListener(contactSyncListener);
            contactSyncListener = null;
        }

        if (blackListSyncListener != null) {
            IMHelper.getInstance().removeSyncBlackListListener(blackListSyncListener);
        }

        if (contactInfoSyncListener != null) {
            IMHelper.getInstance().getUserProfileManager().removeSyncContactInfoListener(contactInfoSyncListener);
        }
    }


    protected class HeaderItemClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.application_item:
                    // 进入申请与通知页面
                    startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
                    break;
//                case R.id.group_item:
//                    // 进入群聊列表页面
//                    startActivity(new Intent(getActivity(), GroupsActivity.class));
//                    break;
//                case R.id.chat_room_item:
//                    //进入聊天室列表页面
//                    startActivity(new Intent(getActivity(), PublicChatRoomsActivity.class));
//                    break;
//                case R.id.robot_item:
//                    //进入Robot列表页面
//                    startActivity(new Intent(getActivity(), RobotsActivity.class));
//                    break;

                default:
                    break;
            }
        }

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        toBeProcessUser = (EaseUser) listView.getItemAtPosition(((AdapterContextMenuInfo) menuInfo).position);
        toBeProcessUsername = toBeProcessUser.getUsername();
        getActivity().getMenuInflater().inflate(R.menu.em_context_contact_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_contact) {
            try {
                // 删除此联系人
                deleteContact(toBeProcessUser);
                // 删除相关的邀请消息
                InviteMessgeDao dao = new InviteMessgeDao(getActivity());
                dao.deleteMessage(toBeProcessUser.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
//        else if (item.getItemId() == R.id.add_to_blacklist) {
//            moveToBlacklist(toBeProcessUsername);
//            return true;
//        }
        return super.onContextItemSelected(item);
    }


    /**
     * 删除联系人
     */
    public void deleteContact(final EaseUser tobeDeleteUser) {
        ((BaseActivity) getActivity()).showProgressDialog();
        String friendId = tobeDeleteUser.getUsername();
        Response.Listener<ApiResponse> successListener = new Response.Listener<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse response) {

                final String st2 = getResources().getString(R.string.Delete_failed);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager().deleteContact(tobeDeleteUser.getUsername());
                            // 删除db和内存中此用户的数据
                            UserDao dao = new UserDao(getActivity());
                            dao.deleteContact(tobeDeleteUser.getUsername());
                            IMHelper.getInstance().getContactList().remove(tobeDeleteUser.getUsername());
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    ((BaseActivity) getActivity()).hideProgressDialog();
                                    contactList.remove(tobeDeleteUser);
                                    contactListLayout.refresh();

                                }
                            });
                        } catch (final Exception e) {
                            ((BaseActivity) getActivity()).hideProgressDialog();
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getActivity(), st2 + e.getMessage(), 1).show();
                                }
                            });

                        }

                    }
                }).start();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) getActivity()).hideProgressDialog();
            }
        };

        ApiHelper.deleteFriend(getActivity(), friendId, ApiResponse.class, successListener, errorListener);
    }

    class ContactSyncListener implements DataSyncListener {
        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contact list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (success) {
                                loadingView.setVisibility(View.GONE);
                                refresh();
                            } else {
                                String s1 = getResources().getString(R.string.get_failed_please_check);
                                Toast.makeText(getActivity(), s1, 1).show();
                                loadingView.setVisibility(View.GONE);
                            }
                        }

                    });
                }
            });
        }
    }

    class BlackListSyncListener implements DataSyncListener {

        @Override
        public void onSyncComplete(boolean success) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    refresh();
                }
            });
        }

    }

    ;

    class ContactInfoSyncListener implements DataSyncListener {

        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contactinfo list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    loadingView.setVisibility(View.GONE);
                    if (success) {
                        refresh();
                    }
                }
            });
        }

    }

}

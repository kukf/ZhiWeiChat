package com.doohaa.chat.ui.AddContact;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hyphenate.chat.EMClient;
import com.doohaa.chat.IMHelper;
import com.doohaa.chat.R;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.AddContactDto;
import com.doohaa.chat.api.dto.ContactInfo;
import com.doohaa.chat.api.dto.SearchUserDto;
import com.doohaa.chat.api.dto.UserDto;
import com.doohaa.chat.preferences.UserPreferences;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.ui.common.OCToast;
import com.doohaa.chat.utils.Constants;
import com.doohaa.chat.utils.Validator;
import com.hyphenate.easeui.widget.EaseAlertDialog;

import java.util.ArrayList;

public class AddContactActivity extends BaseActivity implements OnClickListener, TextWatcher {

    private EditText editText;
    private LinearLayout searchedUserLayout;
    private TextView nameText, mTextView;
    private Button searchBtn;
    private Button btnApplyFriend;
    private ImageView avatar;
    private InputMethodManager inputMethodManager;
    private String toAddUsername;
    private ContactHelper contactHelper;

    private ListView listView;
    private MyAdapter adapter;

    private UserDto searchResultUser;
    private String searchReasultUserId;
    private String name;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_add_contact);
        initComponent();
        registEvent();
        getContactList();
        hideSoftKeyboard();
    }

    private void initComponent() {
        mTextView = (TextView) findViewById(R.id.add_list_friends);
        editText = (EditText) findViewById(R.id.edit_note);
        searchedUserLayout = (LinearLayout) findViewById(R.id.ll_user);
        nameText = (TextView) findViewById(R.id.name);
        searchBtn = (Button) findViewById(R.id.search);
        avatar = (ImageView) findViewById(R.id.avatar);
        btnApplyFriend = (Button) findViewById(R.id.apply_friend);
        listView = (ListView) findViewById(R.id.contact_list);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        contactHelper = new ContactHelper(AddContactActivity.this);

        searchResultUser = new UserDto();
        id = UserPreferences.getInstance(AddContactActivity.this).getUser().getId() + "";
        name = UserPreferences.getInstance(AddContactActivity.this).getUserName();
    }

    private void registEvent() {
        searchBtn.setOnClickListener(this);
        btnApplyFriend.setOnClickListener(this);
        editText.addTextChangedListener(this);
    }


    private void getContactList() {
        showProgressDialog();
        ContactHelper.QueryFinishListener queryFinishListener = new ContactHelper.QueryFinishListener() {
            @Override
            public void onQueryFinished(ArrayList<ContactInfo> localContactList) {
                hideProgressDialog();
                adapter = new MyAdapter(AddContactActivity.this, localContactList);
                listView.setAdapter(adapter);
            }
        };
        contactHelper.setQueryFinishListener(queryFinishListener);
        contactHelper.queryContactList();
    }

    private void getContactListRegistState(ArrayList<ContactInfo> localContactList) {

    }


    private void searchContact() {
        final String searchId = editText.getText().toString();
        if (Validator.isEmpty(searchId)) {
            return;
        }
        showProgressDialog();
        Response.Listener<SearchUserDto> successListener = new Response.Listener<SearchUserDto>() {
            @Override
            public void onResponse(SearchUserDto response) {
                hideProgressDialog();
                //        //服务器存在此用户，显示此用户和添加按钮
                if (Validator.isNotEmpty(response)) {
                    if (Validator.isNotEmpty(response)) {
                        searchResultUser = response.getData();
                        if (Validator.isNotEmpty(searchResultUser)) {
                            searchedUserLayout.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                            nameText.setText(searchResultUser.getName());
                            searchReasultUserId = searchResultUser.getFkMemberId() + "";
                        }
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
            }
        };

        if (searchId.length() == 11) {
            ApiHelper.getUserProfile(AddContactActivity.this, "", searchId, SearchUserDto.class, successListener, errorListener);
        } else {
            ApiHelper.getUserProfile(AddContactActivity.this, searchId, "", SearchUserDto.class, successListener, errorListener);
        }
    }

    /**
     * 添加contact
     */
    private void addContact() {
        if (Validator.isEmpty(searchReasultUserId)) {
            return;
        }

        if (EMClient.getInstance().getCurrentUser().equals(searchReasultUserId)) {
            new EaseAlertDialog(this, R.string.not_add_myself).show();
            return;
        }

        if (IMHelper.getInstance().getContactList().containsKey(searchReasultUserId)) {
            //提示已在好友列表中(在黑名单列表里)，无需添加
            if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(searchReasultUserId)) {
                new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
                return;
            }
            new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
            return;
        }

        showProgressDialog();
        Response.Listener<AddContactDto> successListener = new Response.Listener<AddContactDto>() {
            @Override
            public void onResponse(AddContactDto response) {
                final String message = getString(R.string.Add_a_friend) + Constants.INVITED_SEPATATOR + name + Constants.INVITED_SEPATATOR + response.getData();
//                OCToast.makeToast(getApplicationContext(), R.string.apply_friend_success, Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    public void run() {

                        try {
                            EMClient.getInstance().contactManager().addContact(searchReasultUserId, message);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    hideProgressDialog();
                                    String s1 = getResources().getString(R.string.send_successful);
                                    OCToast.makeToast(getApplicationContext(), s1, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (final Exception e) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    hideProgressDialog();
                                    String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                                    OCToast.makeToast(getApplicationContext(), s2, Toast.LENGTH_SHORT).show();
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
                hideProgressDialog();
                OCToast.makeToast(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        ApiHelper.applyFriend(AddContactActivity.this, searchReasultUserId, getString(R.string.Add_a_friend), AddContactDto.class, successListener, errorListener);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() < 1) {
            searchedUserLayout.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            hideSoftKeyboard();
        }
    }

    class MyAdapter extends BaseAdapter {
        ArrayList<ContactInfo> localContactList;
        Context context;
        LayoutInflater layoutInflater;

        public MyAdapter(Context context, ArrayList<ContactInfo> localContactList) {
            this.context = context;
            this.localContactList = localContactList;
            this.layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return localContactList == null ? 0 : localContactList.size();
        }

        @Override
        public Object getItem(int position) {
            return localContactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.contact_list_item, null);
                holder = new ViewHolder();
                holder.txtName = (TextView) convertView.findViewById(R.id.name);
                holder.txtPhone = (TextView) convertView.findViewById(R.id.phone);
                holder.btnInvite = (Button) convertView.findViewById(R.id.invite);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ContactInfo contactInfo = localContactList.get(position);
            holder.txtName.setText(contactInfo.getName());
            holder.txtPhone.setText(contactInfo.getNumber());
            holder.btnInvite.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = getString(R.string.invite_msg);
                    doSendSMSTo(contactInfo.getNumber(), msg);
                }
            });
            return convertView;
        }

        /**
         * 调起系统发短信功能
         *
         * @param phoneNumber
         * @param message
         */
        public void doSendSMSTo(String phoneNumber, String message) {
            if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
                intent.putExtra("sms_body", message);
                startActivity(intent);
            }
        }
    }

    class ViewHolder {
        TextView txtName;
        TextView txtPhone;
        Button btnInvite;
    }

    public void back(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                searchContact();
                break;

            case R.id.apply_friend:
                addContact();
                break;
        }
    }
}

package com.doohaa.chat.parse;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.doohaa.chat.IMHelper;
import com.google.gson.Gson;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.doohaa.chat.IMApplication;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.FriendListDto;
import com.doohaa.chat.api.dto.UserDto;
import com.doohaa.chat.utils.PreferenceManager;
import com.doohaa.chat.utils.UIUtils;
import com.hyphenate.easeui.domain.EaseUser;

import android.content.Context;

public class UserProfileManager {

    /**
     * application context
     */
    protected Context appContext = null;

    /**
     * init flag: test if the sdk has been inited before, we don't need to init
     * again
     */
    private boolean sdkInited = false;

    /**
     * HuanXin sync contact nick and avatar listener
     */
    private List<IMHelper.DataSyncListener> syncContactInfosListeners;

    private boolean isSyncingContactInfosWithServer = false;

    private EaseUser currentUser;

    public UserProfileManager() {
    }

    public synchronized boolean init(Context context) {
        if (sdkInited) {
            return true;
        }
        ParseManager.getInstance().onInit(context);
        syncContactInfosListeners = new ArrayList<IMHelper.DataSyncListener>();
        sdkInited = true;
        return true;
    }

    public void addSyncContactInfoListener(IMHelper.DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncContactInfosListeners.contains(listener)) {
            syncContactInfosListeners.add(listener);
        }
    }

    public void removeSyncContactInfoListener(IMHelper.DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncContactInfosListeners.contains(listener)) {
            syncContactInfosListeners.remove(listener);
        }
    }

    public void asyncFetchContactInfosFromServer(List<String> usernames, final EMValueCallBack<List<EaseUser>> callback) {
        if (isSyncingContactInfosWithServer) {
            return;
        }
        isSyncingContactInfosWithServer = true;

//        ParseManager.getInstance().getContactInfos(usernames, new EMValueCallBack<List<EaseUser>>() {
//
//            @Override
//            public void onSuccess(List<EaseUser> value) {
//                isSyncingContactInfosWithServer = false;
//                // in case that logout already before server returns,we should
//                // return immediately
//                if (!IMHelper.getInstance().isLoggedIn()) {
//                    return;
//                }
//                if (callback != null) {
//                    callback.onSuccess(value);
//                }
//            }
//
//            @Override
//            public void onError(int error, String errorMsg) {
//                isSyncingContactInfosWithServer = false;
//                if (callback != null) {
//                    callback.onError(error, errorMsg);
//                }
//            }
//
//        });

        Response.Listener<FriendListDto> successListener = new Response.Listener<FriendListDto>() {
            @Override
            public void onResponse(FriendListDto response) {
                // in case that logout already before server returns,we should
                // return immediately
                if (!IMHelper.getInstance().isLoggedIn()) {
                    return;
                }
                List<EaseUser> easeUsers = new ArrayList<>();
                List<UserDto> userDtos = response.getData();
                for (UserDto userDto : userDtos) {
                    EaseUser easeUser = new EaseUser(userDto.getFkMemberId() + "");
                    easeUser.setNick(userDto.getName());
                    easeUser.setAvatar(UIUtils.getImageUrl(userDto));
                    easeUsers.add(easeUser);
                }
                if (callback != null) {
                    callback.onSuccess(easeUsers);
                }
                isSyncingContactInfosWithServer = false;
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isSyncingContactInfosWithServer = false;
                if (callback != null) {
                    callback.onError(0, error.getMessage());
                }
            }
        };
        ApiHelper.profileList(IMApplication.getInstance().getApplicationContext(), new Gson().toJson(usernames), FriendListDto.class, successListener, errorListener);
//        ApiHelper.friendList(IMApplication.getInstance().getApplicationContext(), FriendListDto.class, successListener, errorListener);
    }

    public void notifyContactInfosSyncListener(boolean success) {
        for (IMHelper.DataSyncListener listener : syncContactInfosListeners) {
            listener.onSyncComplete(success);
        }
    }

    public boolean isSyncingContactInfoWithServer() {
        return isSyncingContactInfosWithServer;
    }

    public synchronized void reset() {
        isSyncingContactInfosWithServer = false;
        currentUser = null;
        PreferenceManager.getInstance().removeCurrentUserInfo();
    }

    public synchronized EaseUser getCurrentUserInfo() {
        if (currentUser == null) {
            String username = EMClient.getInstance().getCurrentUser();
            currentUser = new EaseUser(username);
            String nick = getCurrentUserNick();
            currentUser.setNick((nick != null) ? nick : username);
            currentUser.setAvatar(getCurrentUserAvatar());
        }
        return currentUser;
    }

    public boolean updateCurrentUserNickName(final String nickname) {
        boolean isSuccess = ParseManager.getInstance().updateParseNickName(nickname);
        if (isSuccess) {
            setCurrentUserNick(nickname);
        }
        return isSuccess;
    }

    public String uploadUserAvatar(String avatarUrl) {
//        String avatarUrl = ParseManager.getInstance().uploadParseAvatar(data);
//        if (avatarUrl != null) {
        setCurrentUserAvatar(avatarUrl);
//        }
        return avatarUrl;
    }

//    public void asyncGetCurrentUserInfo() {
//        ParseManager.getInstance().asyncGetCurrentUserInfo(new EMValueCallBack<EaseUser>() {
//
//            @Override
//            public void onSuccess(EaseUser value) {
//                if (value != null) {
//                    setCurrentUserNick(value.getNick());
//                    setCurrentUserAvatar(value.getAvatar());
//                }
//            }
//
//            @Override
//            public void onError(int error, String errorMsg) {
//
//            }
//        });
//    }

    public void asyncGetUserInfo(final String username, final EMValueCallBack<EaseUser> callback) {
        ParseManager.getInstance().asyncGetUserInfo(username, callback);
    }

    private void setCurrentUserNick(String nickname) {
        getCurrentUserInfo().setNick(nickname);
        PreferenceManager.getInstance().setCurrentUserNick(nickname);
    }

    private void setCurrentUserAvatar(String avatar) {
        getCurrentUserInfo().setAvatar(avatar);
        PreferenceManager.getInstance().setCurrentUserAvatar(avatar);
    }

    private String getCurrentUserNick() {
        return PreferenceManager.getInstance().getCurrentUserNick();
    }

    private String getCurrentUserAvatar() {
        return PreferenceManager.getInstance().getCurrentUserAvatar();
    }

}

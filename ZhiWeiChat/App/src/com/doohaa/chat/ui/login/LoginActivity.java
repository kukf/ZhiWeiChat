/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
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
package com.doohaa.chat.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.doohaa.chat.IMApplication;
import com.doohaa.chat.IMHelper;
import com.doohaa.chat.R;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.LoginDataDto;
import com.doohaa.chat.api.dto.LoginDto;
import com.doohaa.chat.api.dto.UserDto;
import com.doohaa.chat.db.DemoDBManager;
import com.doohaa.chat.preferences.LoginPreferences;
import com.doohaa.chat.preferences.TokenPreferences;
import com.doohaa.chat.preferences.UserPreferences;
import com.doohaa.chat.ui.MainActivity;
import com.doohaa.chat.ui.RegisterActivity;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.utils.Validator;
import com.hyphenate.easeui.utils.EaseCommonUtils;

/**
 * 登陆页面
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    public static final int REQUEST_CODE_SETNICK = 1;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private boolean autoLogin = false;

    private String currentUsername;
    private String currentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSoftKeyboard();
        // 如果登录成功过，直接进入主页面
        if (IMHelper.getInstance().isLoggedIn()) {
            autoLogin = true;
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            return;
        }
        setContentView(R.layout.em_activity_login);

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        // 如果用户名改变，清空密码
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordEditText.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 登录
     */
    public void login(View view) {
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentUsername = usernameEditText.getText().toString().trim();
        currentPassword = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(currentUsername)) {
            showMessage(R.string.User_name_cannot_be_empty);
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            showMessage(R.string.Password_cannot_be_empty);
            return;
        }

        showProgressDialog();
        LoginPreferences.getInstance(LoginActivity.this).setLoginName(currentUsername);
        Response.Listener<LoginDto> successListener = new Response.Listener<LoginDto>() {
            @Override
            public void onResponse(LoginDto response) {
                if (Validator.isNotEmpty(response)) {
                    boolean success = response.isSuccess();
                    if (success) {
                        LoginDataDto data = response.getData();
                        String token = data.getToken();
                        UserDto user = data.getMemberProfile();
                        UserPreferences.getInstance(LoginActivity.this).setUser(user);
                        TokenPreferences.getInstance(LoginActivity.this).setToken(token);
                        if (Validator.isNotEmpty(user)) {
                            loginToIM(user.getFkMemberId() + "");
                        } else {
                            showMessage(R.string.login_server_fail);
                            hideProgressDialog();
                        }

                    } else {
                        hideProgressDialog();
                        showMessage(response.getMessage());
                    }
                } else {
                    showMessage(R.string.login_server_fail);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                showMessage(error.getMessage());
            }
        };

        ApiHelper.login(LoginActivity.this, currentUsername, currentPassword, LoginDto.class, successListener, errorListener);
    }

    public void loginToIM(String userName) {

        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        IMHelper.getInstance().setCurrentUserName(userName);

        final long start = System.currentTimeMillis();
        // 调用sdk登陆方法登陆聊天服务器
        Log.d(TAG, "EMClient.getInstance().login");
        EMClient.getInstance().login(userName, "zhiweiim", new EMCallBack() {

            @Override
            public void onSuccess() {
                long end = System.currentTimeMillis();
//                long start1 = System.currentTimeMillis();
//                IMHelper.getInstance().resetCurrentUser();
//                long end1 = System.currentTimeMillis();
//                Log.e("xxx", "时间1 ==》 " + (end1 - start1));
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                        // ** manually load all local groups and
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();

                        // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                        boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                                IMApplication.currentUserNick.trim());
                        if (!updatenick) {
                            Log.e("LoginActivity", "update current user nick fail");
                        }
                        //异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
//                        IMHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                        // 进入主页面
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);

                        finish();
                    }
                });
                Log.d(TAG, "login: onSuccess");
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        showMessage(getString(R.string.Login_failed) + message);
                        finish();
                    }
                });
            }
        });
    }


    /**
     * 注册
     *
     * @param view
     */
    public void register(View view) {
        startActivityForResult(new Intent(this, RegisterActivity.class), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (autoLogin) {
            return;
        }
    }
}

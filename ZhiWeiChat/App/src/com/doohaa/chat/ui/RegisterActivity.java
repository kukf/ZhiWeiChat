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
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.doohaa.chat.api.dto.RegistDataDto;
import com.doohaa.chat.api.dto.RegistDto;
import com.doohaa.chat.api.dto.SendCodeDto;
import com.doohaa.chat.db.DemoDBManager;
import com.doohaa.chat.preferences.TokenPreferences;
import com.doohaa.chat.preferences.UserPreferences;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.ui.login.LoginActivity;
import com.doohaa.chat.utils.Validator;
import com.doohaa.chat.utils.ValidatorTool;
import com.hyphenate.easeui.utils.EaseCommonUtils;

/**
 * 注册页
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";
    private EditText edtPhone;
    private EditText edtCode;
    private Button btnSendCode;
    private EditText edtUserName;
    private EditText edtPassword;
    private EditText edtConfirm;
    private EditText edtInviter;
    private Button btnRegist;

    // 验证码相关
    private static final int TOTAL_TIME = 60000;
    private static final int INTERVAL_TIME = 1000;
    private static final String ONSAVEINSTANCE_TIME_KEY = "onsaveinstance_time_key";
    private static final String MILLIS_UNTI_FINISHED_KEY = "millis_until_finished_key";
    private boolean isCounting = false;
    private long millisUntilFinished = 0;
    private TimeCount timeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_register);
        hideSoftKeyboard();
        initComponent();
        registEvent();

        if (savedInstanceState != null) {
            long saveinstanceTime = savedInstanceState.getLong(ONSAVEINSTANCE_TIME_KEY);
            long millisUntilFinished = savedInstanceState.getLong(MILLIS_UNTI_FINISHED_KEY);

            long currentTime = System.currentTimeMillis();
            long dif = currentTime - saveinstanceTime;

            if (dif >= 0) {
                long finishDif = millisUntilFinished - dif;
                if (finishDif >= 0) {
                    timeCount = new TimeCount(finishDif, INTERVAL_TIME);
                    isCounting = true;
                    timeCount.start();
                }
            }
        }
    }

    private void initComponent() {
        edtPhone = (EditText) findViewById(R.id.phone);
        edtCode = (EditText) findViewById(R.id.code);
        btnSendCode = (Button) findViewById(R.id.send_code);
        edtUserName = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        edtConfirm = (EditText) findViewById(R.id.confirm_password);
        edtInviter = (EditText) findViewById(R.id.inviter);
        btnRegist = (Button) findViewById(R.id.regist);
    }

    private void registEvent() {
        btnRegist.setOnClickListener(this);
        btnSendCode.setOnClickListener(this);
    }

    private void sendCode() {
        String phone = edtPhone.getText().toString().trim();
        if (Validator.isEmpty(phone)) {
            showMessage(getResources().getString(R.string.phone_cannot_be_empty));
            edtPhone.requestFocus();
            return;
        }

        if (isCounting) {
            return;
        }

        showProgressDialog();
        Response.Listener<SendCodeDto> successListener = new Response.Listener<SendCodeDto>() {
            @Override
            public void onResponse(SendCodeDto response) {
                hideProgressDialog();
                if (Validator.isNotEmpty(response)) {
                    boolean success = response.isSuccess();
                    if (success) {
                        timeCount = new TimeCount(TOTAL_TIME, INTERVAL_TIME);
                        timeCount.start();
                        showMessage(R.string.send_code_success);
//                        edtCode.setText(response.getData());
                    } else {
                        showMessage(response.getMessage());
                    }
                } else {
                    showMessage(R.string.send_code_fail);
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

        ApiHelper.seCode(RegisterActivity.this, phone, SendCodeDto.class, successListener, errorListener);
    }

    private void register() {
        String phone = edtPhone.getText().toString().trim();
        String code = edtCode.getText().toString().trim();
        String userName = edtUserName.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirm = edtConfirm.getText().toString().trim();
        String inviter = edtInviter.getText().toString().trim();
        if (Validator.isEmpty(phone) || !ValidatorTool.isMobileNO(phone)) {
            showMessage(getResources().getString(R.string.phone_cannot_be_empty));
            edtPhone.requestFocus();
            return;
        } else if (Validator.isEmpty(code) || !ValidatorTool.isCode(code)) {
            showMessage(getResources().getString(R.string.code_cannot_be_empty));
            edtCode.requestFocus();
            return;
        } else if (Validator.isEmpty(userName)) {
            showMessage(getResources().getString(R.string.User_name_cannot_be_empty));
            edtUserName.requestFocus();
            return;
        } else if (Validator.isEmpty(password) || !ValidatorTool.isPassWord(password)) {
            showMessage(getResources().getString(R.string.Password_cannot_be_empty));
            edtPassword.requestFocus();
            return;
        } else if (Validator.isEmpty(confirm)) {
            showMessage(getResources().getString(R.string.Confirm_password_cannot_be_empty));
            edtConfirm.requestFocus();
            return;
        } else if (!password.equals(confirm)) {
            showMessage(getResources().getString(R.string.Two_input_password));
            return;
        }

        showProgressDialog();
        Response.Listener<RegistDto> successListener = new Response.Listener<RegistDto>() {
            @Override
            public void onResponse(RegistDto response) {
                if (Validator.isNotEmpty(response)) {
                    boolean success = response.isSuccess();
                    if (success) {
                        RegistDataDto data = response.getData();
                        if (Validator.isNotEmpty(data)) {
                            TokenPreferences.getInstance(RegisterActivity.this).setToken(data.getToken());
                            UserPreferences.getInstance(RegisterActivity.this).setUser(data.getMemberProfile());
                            loginToIM(data.getMemberProfile().getFkMemberId() + "");
                        } else {
                            hideProgressDialog();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }
                    } else {
                        hideProgressDialog();
                        showMessage(response.getMessage());
                    }
                } else {
                    hideProgressDialog();
                    showMessage(R.string.regist_fail);
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

        ApiHelper.register(RegisterActivity.this, code, phone, password, inviter, userName, RegistDto.class, successListener, errorListener);

    }

    public void loginToIM(String userName) {
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            hideProgressDialog();
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
//                IMHelper.getInstance().resetCurrentUser();
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        Log.d(TAG, "loginIM: onSuccess");

                        // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                        // ** manually load all local groups and
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();

                        // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                        boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                                IMApplication.currentUserNick.trim());
                        if (!updatenick) {
                            Log.e("RegisterActivity", "update current user nick fail");
                        }
                        //异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
//                        IMHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                        // 进入主页面
                        Intent intent = new Intent(RegisterActivity.this,
                                MainActivity.class);
                        startActivity(intent);

                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ONSAVEINSTANCE_TIME_KEY, System.currentTimeMillis());
        outState.putLong(MILLIS_UNTI_FINISHED_KEY, millisUntilFinished);
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            RegisterActivity.this.millisUntilFinished = millisUntilFinished;
            btnSendCode.setText(millisUntilFinished / 1000 + "s");
            isCounting = true;
        }

        @Override
        public void onFinish() {
            btnSendCode.setText(R.string.send_code);
            isCounting = false;
        }
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regist:
                register();
                break;

            case R.id.send_code:
                sendCode();
                break;
        }
    }
}

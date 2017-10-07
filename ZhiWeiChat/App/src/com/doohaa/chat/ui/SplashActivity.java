package com.doohaa.chat.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hyphenate.chat.EMClient;
import com.doohaa.chat.IMHelper;
import com.doohaa.chat.R;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.CheckVersionDataDto;
import com.doohaa.chat.api.dto.CheckVersionDto;
import com.doohaa.chat.preferences.TokenPreferences;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.ui.common.OCCommonPopup;
import com.doohaa.chat.ui.common.OCCommonPopupConstants;
import com.doohaa.chat.ui.login.LoginActivity;
import com.doohaa.chat.utils.Validator;

/**
 * 开屏页
 */
public class SplashActivity extends BaseActivity {
    private RelativeLayout rootLayout;

    private static final int sleepTime = 2000;
    private OCCommonPopup commonPopup;

    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.em_activity_splash);
        super.onCreate(arg0);

        rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        rootLayout.startAnimation(animation);
        checkVersion();
    }

    private void checkVersion() {
        Response.Listener<CheckVersionDto> successListener = new Response.Listener<CheckVersionDto>() {
            @Override
            public void onResponse(CheckVersionDto response) {
                if (Validator.isNotEmpty(response)) {
                    CheckVersionDataDto checkVersionDataDto = response.getData();
                    if (Validator.isNotEmpty(checkVersionDataDto)) {
                        boolean shouldUpdate = checkVersionDataDto.isType();
                        if (shouldUpdate) {
                            showUpdateVersion(checkVersionDataDto.getUrl());
                        } else {
                            loadLocalData();
                        }
                    } else {
                        showMessage(R.string.check_version_fail);
                        finish();
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage(R.string.check_version_fail);
                finish();
            }
        };

        String versionCode = getAppVersionCode();
        if (Validator.isNotEmpty(versionCode)) {
            ApiHelper.checkVersion(SplashActivity.this, versionCode, CheckVersionDto.class, successListener, errorListener);
        }
    }

    private void showUpdateVersion(final String url) {
        if (Validator.isNotEmpty(commonPopup)) {
            commonPopup.dismiss();
        }
        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        };

        DialogInterface.OnDismissListener onDismissListener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    showMessage(R.string.check_version_fail);
                    finish();
                }
            }
        };
        OCCommonPopup.Builder builder = new OCCommonPopup.Builder(SplashActivity.this, OCCommonPopupConstants.COMMON_ONE_BUTTON);
        String message = getResources().getString(R.string.update);
        builder.setMessage(message);
        builder.setOnPositiveListener(positiveListener);
        builder.setOnDismissListener(onDismissListener);
        commonPopup = builder.create();
        commonPopup.show();
    }

    private String getAppVersionCode() {
        try {
            String pkName = this.getPackageName();
            int versionCode = this.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return versionCode + "";
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void loadLocalData() {
        new Thread(new Runnable() {
            public void run() {
                boolean isAlreadyLogin = TokenPreferences.getInstance(SplashActivity.this).isAlreadyLogin();
                if (isAlreadyLogin) {
                    if (IMHelper.getInstance().isLoggedIn()) {
                        // ** 免登陆情况 加载所有本地群和会话
                        //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
                        //加上的话保证进了主页面会话和群组都已经load完毕
                        long start = System.currentTimeMillis();
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        long costTime = System.currentTimeMillis() - start;
                        //等待sleeptime时长
                        if (sleepTime - costTime > 0) {
                            try {
                                Thread.sleep(sleepTime - costTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
//                        //进入主页面
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                        }
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
//                if (IMHelper.getInstance().isLoggedIn()) {
//                    // ** 免登陆情况 加载所有本地群和会话
//                    //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
//                    //加上的话保证进了主页面会话和群组都已经load完毕
//                    long start = System.currentTimeMillis();
//                    EMClient.getInstance().groupManager().loadAllGroups();
//                    EMClient.getInstance().chatManager().loadAllConversations();
//                    long costTime = System.currentTimeMillis() - start;
//                    //等待sleeptime时长
//                    if (sleepTime - costTime > 0) {
//                        try {
//                            Thread.sleep(sleepTime - costTime);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    //进入主页面
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                    finish();
//                } else {
//                    try {
//                        Thread.sleep(sleepTime);
//                    } catch (InterruptedException e) {
//                    }
//                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                    finish();
//                }
            }
        }).start();
    }

    /**
     * 获取当前应用程序的版本号
     */
    private String getVersion() {
        return EMClient.getInstance().VERSION;
    }
}

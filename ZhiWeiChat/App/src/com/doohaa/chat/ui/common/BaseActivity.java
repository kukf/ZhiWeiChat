package com.doohaa.chat.ui.common;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.doohaa.chat.R;
import com.doohaa.chat.api.dto.ApiResponse;
import com.doohaa.chat.ui.login.LoginActivity;
import com.doohaa.chat.ui.login.LoginConstants;
import com.doohaa.chat.utils.OCLog;
import com.doohaa.chat.utils.StringUtils;
import com.doohaa.chat.utils.Validator;
import com.doohaa.chat.volley.MyVolley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyphenate.easeui.ui.EaseBaseActivity;

import java.util.List;

public class BaseActivity extends EaseBaseActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    private int progressDialogRefCount = 0;
    private Dialog signUpPopup;
    protected Gson gson;
    protected boolean activityDestroyed = false;

    private boolean isActivityLoadingRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new GsonBuilder().create();
        OCLog.i(TAG, String.format("%s.onCreate()", getClass().getSimpleName()));
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        OCLog.i(TAG, String.format("%s.onNewIntent()", getClass().getSimpleName()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        OCLog.i(TAG, String.format("%s.onStart()", getClass().getSimpleName()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        OCLog.i(TAG, String.format("%s.onResume()", getClass().getSimpleName()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        OCLog.i(TAG, String.format("%s.onPause()", getClass().getSimpleName()));

        cancelRequest();
    }

    @Override
    protected void onStop() {
        super.onStop();
        OCLog.i(TAG, String.format("%s.onStop()", getClass().getSimpleName()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityDestroyed = true;
        OCLog.i(TAG, String.format("%s.onDestroy()", getClass().getSimpleName()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        OCLog.i(TAG, String.format("%s.onSaveInstanceState()", getClass().getSimpleName()));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        OCLog.i(TAG, String.format("%s.onRestoreInstanceState()", getClass().getSimpleName()));
    }

    public void showErrorToast(VolleyError error) {
        if (error == null || StringUtils.isBlank(error.getMessage())) {
            return;
        }
        Toast.makeText(BaseActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /**
     *
     */
    public void showProgressDialog() {
        showProgressDialog(true, false, null);
    }

    public void showProgressDialog(String tag) {
        showProgressDialog(true, false, tag);
    }

    /**
     * @param cancelable
     */
    public void showProgressDialog(boolean cancelable) {
        showProgressDialog(cancelable, false, null);
    }

    /**
     * @param cancelable
     * @param touchable
     */
    public void showProgressDialog(boolean cancelable, boolean touchable, String tag) {
        if (progressDialogRefCount == 0) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            ProgressDialogFragment prevDialog = (ProgressDialogFragment) getSupportFragmentManager().findFragmentByTag(ProgressDialogFragment.TAG);
            if (prevDialog != null) {
                fragmentTransaction.remove(prevDialog);
            }

            ProgressDialogFragment progressDialog = ProgressDialogFragment.newInstance(tag);
            progressDialog.setCancelable(cancelable);
            progressDialog.setTouchable(touchable);
            progressDialog.setOnCancelListener(new ProgressDialogFragment.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog, String tag) {
                    hideProgressDialog(true);
                    onProgressCancel(tag);
                }
            });

            fragmentTransaction.add(progressDialog, ProgressDialogFragment.TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }

        progressDialogRefCount++;
    }

    /**
     *
     */
    public void hideProgressDialog() {
        hideProgressDialog(false);
    }

    /**
     * @param immediately
     */
    public void hideProgressDialog(final boolean immediately) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                progressDialogRefCount--;

                if (immediately || progressDialogRefCount <= 0) {
                    progressDialogRefCount = 0;

                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(ProgressDialogFragment.TAG);
                    if (fragment == null) {
                        return;
                    }
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            }
        });

    }

    protected void onProgressCancel(String tag) {
        OCLog.d(TAG, "onProgressCancel()");
        if (StringUtils.isEmpty(tag)) {
            //TODO
        } else {
            cancelRequest(tag);
        }
    }

    private void goTaleLoginActivity(Intent paramIntent, int loginRequestCode) {
        Intent intent = new Intent(this, LoginActivity.class);
        if (paramIntent != null) {
            intent.putExtra(LoginConstants.PARAM_INTENT, paramIntent);
        }
        if (Validator.isNotEmpty(loginRequestCode)) {
            intent.putExtra(CommonConstants.REQUEST_CODE_LOGIN, loginRequestCode);
        }
        startActivityForResult(intent, loginRequestCode);
    }

    private void dismissSignUpPopup() {
        if (Validator.isEmpty(signUpPopup)) {
            return;
        }
        if (signUpPopup.isShowing()) {
            signUpPopup.dismiss();
        }
    }

    public void cancelRequest() {
        OCLog.d("cancelRequest(), call");
        MyVolley.getRequestQueue().cancelAll(this.getClass().getSimpleName());
    }

    public void cancelRequest(String tag) {
        OCLog.d("cancelRequest(), tag : " + tag);
        MyVolley.getRequestQueue().cancelAll(tag);
    }

    public void showMessage(String message) {
        if (Validator.isEmpty(message)) {
            message = getResources().getString(R.string.message_common_error);
        }
        OCToast.makeToast(this, message, Toast.LENGTH_LONG).show();
    }

    public void showMessage(int resId) {

        OCToast.makeToast(this, resId, Toast.LENGTH_LONG).show();
    }

    public void showCommonErrorMessage() {

        OCToast.makeToast(this, R.string.message_common_error, Toast.LENGTH_LONG).show();
    }

    public void showMessage(ApiResponse response) {
        String message;
        message = ((ApiResponse) response).getMessage();
        showMessage(message);
    }

    /**
     * show error popup with message
     *
     * @param message
     */
    private void showErrorPopup(String message) {
        OCCommonPopup.Builder builder = new OCCommonPopup.Builder(this, OCCommonPopupConstants.COMMON_ONE_BUTTON);
        builder.setMessage(message);
        OCCommonPopup commonPopup = builder.create();
        commonPopup.show();
    }

    public boolean isActivityLoadingRefresh() {
        return isActivityLoadingRefresh;
    }

    public void setActivityLoadingRefresh(boolean value) {
        isActivityLoadingRefresh = value;
    }

    public boolean isFinishingDestroyed() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return this.isFinishing() || this.isDestroyed();
        }

        return super.isFinishing() || activityDestroyed;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (Validator.isEmpty(fragments)) {
            return;
        }
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isMenuVisible()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        Log.d(TAG, String.format("resutCode : %s", resultCode == RESULT_OK ? "RESULT_OK" : "RESULT_CANCELED"));
    }

    @Override
    public void back(View view) {
        super.back(view);
    }
}

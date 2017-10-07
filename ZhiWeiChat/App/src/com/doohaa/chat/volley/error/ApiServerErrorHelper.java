package com.doohaa.chat.volley.error;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;
import com.doohaa.chat.ActivityStarter;
import com.doohaa.chat.R;
import com.doohaa.chat.api.ApiErrorCode;
import com.doohaa.chat.api.dto.ApiResponse;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.ui.common.OCCommonPopup;
import com.doohaa.chat.ui.common.OCCommonPopupConstants;
import com.doohaa.chat.ui.common.OCToast;
import com.doohaa.chat.utils.Validator;
import com.doohaa.chat.volley.ApiServerErrorListener;
import com.doohaa.chat.volley.request.GsonRequest;

public class ApiServerErrorHelper<T> {
    private Context context;
    private OCCommonPopup commonPopup;
    private GsonRequest request;
    private ApiServerErrorListener<T> apiServerErrorListener;
    private boolean responseWithCommonError = false;

    public ApiServerErrorHelper(Context context, GsonRequest request, ApiServerErrorListener<T> apiServerErrorListener,
                                boolean responseWithCommonError) {
        this.context = context;
        this.request = request;
        this.apiServerErrorListener = apiServerErrorListener;
        this.responseWithCommonError = responseWithCommonError;
    }

    public void setErrorResponse(T response) {
        if (Validator.isEmpty(request)) {
            return;
        }

        if (Validator.isEmpty(context)) {
            request.deliverError(new ApiServerError((ApiResponse) response));
            return;
        }

        if (!(response instanceof ApiResponse)) {
            request.deliverError(new ParseError());
            return;
        }

        String code = (((ApiResponse) response).getCode());
        // service error
        if (ApiErrorCode.FORCE_UPDATE.equals(code)) {
            responseErrorWithCommonError(response);
            showForceUpdatePopup(response);
        } else if (ApiErrorCode.EXPIRED_TOKEN.equals(code)) {
            responseErrorWithCommonError(response);
            //				AuthHelper.clearUserData(context);
            ActivityStarter.startLoginActivity(context);
            showIllegalTokenToastMessage(((ApiResponse) response).getMessage());
        } else {
            if (Validator.isNotEmpty(apiServerErrorListener)) {
                apiServerErrorListener.onResponse(response);
            } else {
                showDefaultErrorMessage(response);
            }
        }
    }


    private void responseErrorWithCommonError(T response) {
        if (responseWithCommonError) {
            request.deliverError(new ApiServerError((ApiResponse) response));
        }
    }

    private void showDefaultErrorMessage(T response) {
        String message;

        message = ((ApiResponse) response).getMessage();
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).showMessage(message);
        } else {
            showToastMessage(message);
        }
        request.deliverError(new ApiServerError((ApiResponse) response));
    }

    public void showToastMessage(String message) {
        if (Validator.isEmpty(message)) {
            message = context.getResources().getString(R.string.message_common_error);
        }
        OCToast.makeToast(context, message, Toast.LENGTH_LONG).show();
    }

    private void showIllegalTokenToastMessage(String message) {
        if (Validator.isEmpty(message)) {
            return;
        }
        OCToast.makeToast(context, message, Toast.LENGTH_LONG).show();
    }

    private void resetUserInfo() {
        // Clear user preference
        String userName = null;
        long userId = -1;
        // TODO
    }

    /**
     * show recommend update popup
     *
     * @param response
     */
    private void showRecommendUpdatePopup(T response) {
        if (Validator.isNotEmpty(commonPopup)) {
            commonPopup.dismiss();
        }
        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (Validator.isNotEmpty(context)) {
                    ActivityStarter.appFinish(context);
                }
                //				startTailPlayStore();
            }
        };
        OCCommonPopup.Builder builder = new OCCommonPopup.Builder(context, OCCommonPopupConstants.COMMON_TWO_BUTTON);
        if (Validator.isNotEmpty(((ApiResponse) response).getMessage())) {
            String message;
            message = ((ApiResponse) response).getMessage();
            builder.setMessage(message);
        }
        builder.setOnPositiveListener(positiveListener);
        commonPopup = builder.create();
        commonPopup.show();
    }

    /**
     * Application finish
     *
     * @param response
     */
    private void showForceUpdatePopup(T response) {
        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (Validator.isNotEmpty(context)) {
                    ActivityStarter.appFinish(context);
                }
                //				startTailPlayStore();
            }
        };
        DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
                if (Validator.isNotEmpty(context)) {
                    ActivityStarter.appFinish(context);
                }
                //				startTailPlayStore();
            }
        };
        showErrorPopup(response, positiveListener, dismissListener);
        request.deliverError(new VolleyError(((ApiResponse) response).getMessage()));
    }

    /**
     * show error popup
     */
    private void showErrorPopup(T response, DialogInterface.OnClickListener positiveListener,
                                DialogInterface.OnDismissListener dismissListener) {
        if (Validator.isNotEmpty(commonPopup)) {
            commonPopup.dismiss();
        }
        OCCommonPopup.Builder builder = new OCCommonPopup.Builder(context, OCCommonPopupConstants.COMMON_ONE_BUTTON);
        if (Validator.isNotEmpty(((ApiResponse) response).getMessage())) {
            String message;
            message = ((ApiResponse) response).getMessage();
            builder.setMessage(message);
        }

        if (Validator.isNotEmpty(positiveListener)) {
            builder.setOnPositiveListener(positiveListener);
        }
        if (Validator.isNotEmpty(dismissListener)) {
            builder.setOnDismissListener(dismissListener);
        }
        commonPopup = builder.create();
        commonPopup.show();
    }
}

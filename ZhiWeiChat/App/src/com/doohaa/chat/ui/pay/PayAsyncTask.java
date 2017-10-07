package com.doohaa.chat.ui.pay;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.doohaa.chat.api.dto.PayResult;
import com.doohaa.chat.utils.Validator;

import java.util.Map;

/**
 * Created by LittleBear on 2016/9/3.
 */
public class PayAsyncTask extends AsyncTask<String, Void, PayResult> {
    private Context context;
    private PayHelper.OnPayListener onPayListener;

    public PayAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (Validator.isNotEmpty(onPayListener)) {
            onPayListener.onPayStart();
        }
    }

    @Override
    protected PayResult doInBackground(String... params) {
        PayResult payResult;
        PayTask alipay = new PayTask((Activity) context);
        Map<String, String> result = alipay.payV2(params[0], true);
        payResult = new PayResult(result);
        return payResult;
    }

    @Override
    protected void onPostExecute(PayResult payResult) {
        super.onPostExecute(payResult);
        if (Validator.isNotEmpty(onPayListener)) {
            onPayListener.onPayFinish();
        }
        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为9000则代表支付成功
        if (TextUtils.equals(resultStatus, "9000")) {
            if (Validator.isNotEmpty(onPayListener)) {
                onPayListener.onPaySuccess();
            }
        } else {
            if (Validator.isNotEmpty(onPayListener)) {
                onPayListener.onPayFail(resultStatus);
            }
        }
    }

    public void setOnPayListener(PayHelper.OnPayListener onPayListener) {
        this.onPayListener = onPayListener;
    }
}

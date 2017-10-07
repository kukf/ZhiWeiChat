package com.doohaa.chat.ui.pay;

import android.content.Context;
import android.util.Log;

/**
 * Created by LittleBear on 2016/9/3.
 */
public class PayHelper {
    private Context context;
    private OnPayListener onPayListener;

    public interface OnPayListener {
        void onPayStart();

        void onPayFinish();

        void onPayFail(String resultStatus);

        void onPaySuccess();
    }

    public PayHelper(Context context) {
        this.context = context;
    }

    public void pay(String orderInfo) {
        Log.e("xxx", orderInfo);
        PayAsyncTask payAsyncTask = new PayAsyncTask(context);
        payAsyncTask.setOnPayListener(onPayListener);
        payAsyncTask.execute(orderInfo);
    }

    public void setOnPayListener(OnPayListener onPayListener) {
        this.onPayListener = onPayListener;
    }
}

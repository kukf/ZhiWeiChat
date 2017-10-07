package com.doohaa.chat.ui.own;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.doohaa.chat.R;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.RechareDto;
import com.doohaa.chat.ui.EventBus.BaseEvent;
import com.doohaa.chat.ui.EventBus.EventType;
import com.doohaa.chat.ui.EventBus.ModifyUserEvent;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.ui.common.OCToast;
import com.doohaa.chat.ui.pay.PayHelper;
import com.doohaa.chat.utils.Validator;
import com.hyphenate.easeui.widget.EaseTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by sunshixiong on 5/23/16.
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Button btnRecharge;
    private CheckBox chkOneMonth;
    private CheckBox chkThreeMonth;
    private CheckBox chkHalfYear;
    private CheckBox chkOneYear;
    private CheckBox chkTwoYear;
    private CheckBox chkOther;
    private CheckBox chkConfirm;
    private TextView txtSelectResult;
    private EaseTitleBar titleBar;
    private LinearLayout llytPayAlipay;
    private CheckBox chkAlipay;

    private PayHelper payHelper;
    private long payAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_recharge);
        hideSoftKeyboard();
        initComponent();
        registEvent();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    /******
     * EVENT BUS
     ******/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
    }

    private void initComponent() {
        btnRecharge = (Button) findViewById(R.id.btn_recharge);
        chkOneMonth = (CheckBox) findViewById(R.id.one_month);
        chkThreeMonth = (CheckBox) findViewById(R.id.three_month);
        chkHalfYear = (CheckBox) findViewById(R.id.half_year);
        chkOneYear = (CheckBox) findViewById(R.id.one_year);
        chkTwoYear = (CheckBox) findViewById(R.id.two_year);
        chkOther = (CheckBox) findViewById(R.id.other_charge);
        chkConfirm = (CheckBox) findViewById(R.id.agree_disclaimer);
        txtSelectResult = (TextView) findViewById(R.id.time_long);
        titleBar = (EaseTitleBar) findViewById(R.id.title_bar);
        llytPayAlipay = (LinearLayout) findViewById(R.id.pay_alipay);
        chkAlipay = (CheckBox) findViewById(R.id.chk_alipay);

        payHelper = new PayHelper(RechargeActivity.this);
    }

    private void registEvent() {
        btnRecharge.setOnClickListener(this);
        chkOneMonth.setOnCheckedChangeListener(this);
        chkThreeMonth.setOnCheckedChangeListener(this);
        chkHalfYear.setOnCheckedChangeListener(this);
        chkOneYear.setOnCheckedChangeListener(this);
        chkTwoYear.setOnCheckedChangeListener(this);
        chkOther.setOnCheckedChangeListener(this);
        chkConfirm.setOnCheckedChangeListener(this);
        titleBar.setLeftLayoutClickListener(this);
        llytPayAlipay.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recharge:
                recharge();
                break;
            case R.id.left_layout:
                finish();
                break;
            case R.id.pay_alipay:
                payAlipayClick();
                break;
        }
    }

    private void payAlipayClick() {
        chkAlipay.setChecked(!chkAlipay.isChecked());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (timeLongSelectBlank()) {
            txtSelectResult.setText(R.string.time_long_select_blank);
        }

        if (!isChecked) {
            return;
        }
        switch (buttonView.getId()) {
            case R.id.one_month:
//                chkThreeMonth.setChecked(false);
//                chkHalfYear.setChecked(false);
//                chkOneYear.setChecked(false);
//                chkTwoYear.setChecked(false);
//                chkOther.setChecked(false);
//                txtSelectResult.setText(chkOneMonth.getText().toString());
                chkOneMonth.setChecked(false);
//                break;

            case R.id.three_month:
//                chkOneMonth.setChecked(false);
//                chkHalfYear.setChecked(false);
//                chkOneYear.setChecked(false);
//                chkTwoYear.setChecked(false);
//                chkOther.setChecked(false);
//                txtSelectResult.setText(chkThreeMonth.getText().toString());
                chkThreeMonth.setChecked(false);
//                break;
            case R.id.half_year:
//                chkOneMonth.setChecked(false);
//                chkThreeMonth.setChecked(false);
//                chkOneYear.setChecked(false);
//                chkTwoYear.setChecked(false);
//                chkOther.setChecked(false);
//                txtSelectResult.setText(chkHalfYear.getText().toString());
                chkHalfYear.setChecked(false);
//                break;
            case R.id.one_year:
//                chkOneMonth.setChecked(false);
//                chkThreeMonth.setChecked(false);
//                chkHalfYear.setChecked(false);
//                chkTwoYear.setChecked(false);
//                chkOther.setChecked(false);
//                txtSelectResult.setText(chkOneYear.getText().toString());
                chkOneYear.setChecked(false);
//                break;
            case R.id.two_year:
//                chkOneMonth.setChecked(false);
//                chkThreeMonth.setChecked(false);
//                chkHalfYear.setChecked(false);
//                chkOneYear.setChecked(false);
//                chkOther.setChecked(false);
//                txtSelectResult.setText(chkTwoYear.getText().toString());
                chkTwoYear.setChecked(false);
//                break;
                OCToast.makeToast(RechargeActivity.this, R.string.function_note_open, Toast.LENGTH_SHORT).show();
                break;

            case R.id.other_charge:
                chkOneMonth.setChecked(false);
                chkThreeMonth.setChecked(false);
                chkHalfYear.setChecked(false);
                chkOneYear.setChecked(false);
                chkTwoYear.setChecked(false);
                txtSelectResult.setText(chkOther.getText().toString());
                payAmount = 3000;
                break;
            case R.id.agree_disclaimer:

                break;
        }
    }

    private void recharge() {
        if (timeLongSelectBlank()) {
            showMessage(R.string.select_time_long_plz);
            return;
        }

        if (!chkConfirm.isChecked()) {
            showMessage(R.string.agree_disclamier_plz);
            return;
        }

        if (!chkAlipay.isChecked()) {
            showMessage(R.string.pay_select);
            return;
        }

        Response.Listener<RechareDto> successListener = new Response.Listener<RechareDto>() {
            @Override
            public void onResponse(RechareDto response) {
                hideProgressDialog();
                if (Validator.isNotEmpty(response)) {
                    pay(response.getData());
                } else {
                    payFail();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                payFail();
            }
        };

        ApiHelper.recharge(RechargeActivity.this, payAmount, RechareDto.class, successListener, errorListener);
    }

    private void pay(String orderInfo) {
        PayHelper.OnPayListener onPayListener = new PayHelper.OnPayListener() {
            @Override
            public void onPayStart() {

            }

            @Override
            public void onPayFinish() {
                EventBus.getDefault().post(new ModifyUserEvent(EventType.MODIFY_USER, null));
            }

            @Override
            public void onPayFail(String resultStatus) {
                payFail();
            }

            @Override
            public void onPaySuccess() {
                paySuccess();
            }
        };
        payHelper.setOnPayListener(onPayListener);
        payHelper.pay(orderInfo);
    }

    private void paySuccess() {
        OCToast.makeToast(RechargeActivity.this, R.string.pay_success, Toast.LENGTH_SHORT).show();
    }

    private void payFail() {
        OCToast.makeToast(RechargeActivity.this, R.string.pay_fail, Toast.LENGTH_SHORT).show();
    }

    private boolean timeLongSelectBlank() {
        return !chkOneMonth.isChecked() &&
                !chkThreeMonth.isChecked() &&
                !chkHalfYear.isChecked() &&
                !chkOneYear.isChecked() &&
                !chkTwoYear.isChecked() &&
                !chkOther.isChecked();
    }

}

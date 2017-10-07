package com.doohaa.chat.ui.wallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.doohaa.chat.R;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.ApiResponse;
import com.doohaa.chat.api.dto.MemberMoneyAboutDto;
import com.doohaa.chat.api.dto.MemberMoneyAboutResultDto;
import com.doohaa.chat.preferences.UserPreferences;
import com.doohaa.chat.ui.EventBus.BaseEvent;
import com.doohaa.chat.ui.EventBus.EventType;
import com.doohaa.chat.ui.EventBus.ModifyUserEvent;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.ui.common.OCCommonPopup;
import com.doohaa.chat.ui.common.OCCommonPopupConstants;
import com.doohaa.chat.ui.own.OwnActivity;
import com.doohaa.chat.utils.UIUtils;
import com.doohaa.chat.utils.Validator;
import com.hyphenate.easeui.widget.EaseTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by axiong on 2016/9/12.
 */
public class WithDrawalsActivity extends BaseActivity implements View.OnClickListener {
    private EaseTitleBar titleBar;
    private EditText edtAmount;
    private ListView listView;
    private Button btnConfirm;
    private LinearLayout llytApplyWithdrawals;
    private OCCommonPopup commonPopup;

    private List<MemberMoneyAboutResultDto> datas;
    private LayoutInflater layoutInflater;
    private MyAdapter adapter;
    private int payFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals);
        initComponent();
        registEvent();
        getRechargeWithdrawalsHistory();
        EventBus.getDefault().register(this);
        hideSoftKeyboard();
    }

    @Override
    public void onDestroy() {
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
        datas = new ArrayList<>();
        layoutInflater = LayoutInflater.from(this);
        int fromCode = getIntent().getIntExtra("fromCode", 0);
        String title = "";

        llytApplyWithdrawals = (LinearLayout) findViewById(R.id.apply_withdrawals);
        titleBar = (EaseTitleBar) findViewById(R.id.title_bar);
        edtAmount = (EditText) findViewById(R.id.amount);
        listView = (ListView) findViewById(R.id.listview);
        btnConfirm = (Button) findViewById(R.id.confirm);

        switch (fromCode) {
            case 0:
                title = getResources().getString(R.string.apply_withdrawals);
                llytApplyWithdrawals.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);
                payFlag = 2;
                break;

            case 1:
                title = getResources().getString(R.string.withdrawls_log);
                llytApplyWithdrawals.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                payFlag = 2;
                break;

            case 2:
                title = getResources().getString(R.string.rechage_log);
                llytApplyWithdrawals.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                payFlag = 1;
                break;
        }

        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        titleBar.setTitle(title);
    }

    private void registEvent() {
        titleBar.setLeftLayoutClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    private void getRechargeWithdrawalsHistory() {
        showProgressDialog();
        Response.Listener<MemberMoneyAboutDto> successListener = new Response.Listener<MemberMoneyAboutDto>() {
            @Override
            public void onResponse(MemberMoneyAboutDto response) {
                hideProgressDialog();
                if (Validator.isNotEmpty(response)) {
                    List<MemberMoneyAboutResultDto> memberMoneyAboutResultDtos = response.getData();
                    if (Validator.isNotEmpty(memberMoneyAboutResultDtos)) {
                        datas = memberMoneyAboutResultDtos;
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                showErrorToast(error);
            }
        };

        ApiHelper.searchRechageWithdrawals(WithDrawalsActivity.this, payFlag, MemberMoneyAboutDto.class, successListener, errorListener);
    }

    private boolean isNumber(String str) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
        java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

    private void showNeedRealInfoPop() {
        if (Validator.isNotEmpty(commonPopup)) {
            commonPopup.dismiss();
        }
        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(WithDrawalsActivity.this, OwnActivity.class));
            }
        };

        DialogInterface.OnClickListener navListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        OCCommonPopup.Builder builder = new OCCommonPopup.Builder(WithDrawalsActivity.this, OCCommonPopupConstants.NEED_REAL_INFO);

        builder.setOnPositiveListener(positiveListener);
        builder.setOnNegativeListener(navListener);
        commonPopup = builder.create();
        commonPopup.show();
    }

    private void withDrawals(double amount) {
        showProgressDialog();
        Response.Listener<ApiResponse> successListener = new Response.Listener<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse response) {
                EventBus.getDefault().post(new ModifyUserEvent(EventType.MODIFY_USER, null));
                hideProgressDialog();
                showMessage(R.string.apply_withdrawals_success);
                getRechargeWithdrawalsHistory();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                showMessage(R.string.withdrawals_amount_error);
            }
        };

        ApiHelper.withdrawals(WithDrawalsActivity.this, amount, ApiResponse.class, successListener, errorListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;

            case R.id.confirm:
                try {
                    String strAmount = edtAmount.getText().toString();
                    if (Validator.isNotEmpty(strAmount)) {
                        if (isNumber(strAmount)) {
                            double amount = Double.valueOf(strAmount);
                            if (UserPreferences.getInstance(WithDrawalsActivity.this).isRealInfoValide()) {
                                withDrawals(amount);
                            } else {
                                showNeedRealInfoPop();
                            }
                        } else {
                            showMessage(R.string.withdrawals_amount_error);
                        }

                    } else {
                        showMessage(R.string.withdrawals_amount_error);
                    }

                } catch (Exception e) {
                    showMessage(R.string.withdrawals_amount_error);
                }
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.rechage_withdrawals_item, null);
                holder.txtOperate = (TextView) convertView.findViewById(R.id.operate);
                holder.txtAmount = (TextView) convertView.findViewById(R.id.amount);
                holder.txtState = (TextView) convertView.findViewById(R.id.state);
                holder.txtTime = (TextView) convertView.findViewById(R.id.applyTime);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            MemberMoneyAboutResultDto memberMoneyAboutResultDto = datas.get(position);
            int operate = memberMoneyAboutResultDto.getType();
            BigDecimal amount = memberMoneyAboutResultDto.getAmount();
            int state = memberMoneyAboutResultDto.getState();
            long time = memberMoneyAboutResultDto.getApplyTime();


            switch (operate) {
                case 1:
                    holder.txtOperate.setText("充值");
                    break;

                case 2:
                    holder.txtOperate.setText("提现");
                    break;
            }
            holder.txtAmount.setText(amount + "");

            switch (state) {
                case 0:
                    holder.txtState.setText("申请");
                    break;
                case 1:
                    holder.txtState.setText("处理完毕");
                    break;
                case 2:
                    holder.txtState.setText("用户取消");
                    break;
                case 3:
                    holder.txtState.setText("系统拒绝");
                    break;
                case 9:
                    holder.txtState.setText("系统调整");
                    break;
            }
            holder.txtTime.setText(UIUtils.timestamp2Date(time));
            return convertView;
        }
    }

    static class ViewHolder {
        TextView txtOperate;
        TextView txtAmount;
        TextView txtState;
        TextView txtTime;
    }
}

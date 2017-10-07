package com.doohaa.chat.ui.own;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.doohaa.chat.R;
import com.doohaa.chat.api.dto.UserDto;
import com.doohaa.chat.preferences.LoginPreferences;
import com.doohaa.chat.preferences.UserPreferences;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.utils.UIUtils;
import com.doohaa.chat.utils.Validator;
import com.hyphenate.easeui.widget.EaseTitleBar;

/**
 * Created by sunshixiong on 6/7/16.
 */
public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private TextView txtBusinesser;
    private TextView txtCreateTime;
    private TextView txtCreatePhone;
    private EaseTitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_account);
        initComponent();
        registEvent();
        fullOrResetData();
    }


    private void initComponent() {
        txtBusinesser = (TextView) findViewById(R.id.businesser);
        txtCreateTime = (TextView) findViewById(R.id.create_date);
        txtCreatePhone = (TextView) findViewById(R.id.create_phone);
        titleBar = (EaseTitleBar) findViewById(R.id.title_bar);
    }

    private void registEvent() {
        titleBar.setLeftLayoutClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
        }
    }

    private void fullOrResetData() {
        String loginName = LoginPreferences.getInstance(AccountActivity.this).getLoginName();
        UserDto userDto = UserPreferences.getInstance(AccountActivity.this).getUser();
        if (Validator.isNotEmpty(userDto)) {
            String invitor = userDto.getInviter();
            long createTime = userDto.getCreateTime();

            if (Validator.isNotEmpty(loginName)) {
                txtCreatePhone.setText(loginName);
            }

            if (Validator.isNotEmpty(txtBusinesser)) {
                txtBusinesser.setText(invitor);
            }

            if (Validator.isNotEmpty(createTime)) {
                txtCreateTime.setText(UIUtils.timestamp2Date(createTime));
            }

        } else {
            finish();
        }
    }
}
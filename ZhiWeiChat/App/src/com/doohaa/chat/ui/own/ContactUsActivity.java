package com.doohaa.chat.ui.own;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doohaa.chat.R;
import com.doohaa.chat.api.dto.UserDto;
import com.doohaa.chat.preferences.UserPreferences;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.utils.Validator;
import com.hyphenate.easeui.widget.EaseTitleBar;

/**
 * Created by sunshixiong on 6/7/16.
 */
public class ContactUsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llytAddUsContent;
    private LinearLayout llytProblemContent;

    private TextView txtAddUs;
    private TextView txtProblem;

    private ImageView imgExAddUs;
    private ImageView imgExProblem;

    private EaseTitleBar titleBar;

    private int maxDescripLine = 2;
    private boolean isExpandAddUs = false;
    private boolean isExpandProblem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_contact_us);
        initComponent();
        registEvent();
        fullOrResetData();
    }


    private void initComponent() {
        llytAddUsContent = (LinearLayout) findViewById(R.id.add_us_des_content);
        llytProblemContent = (LinearLayout) findViewById(R.id.problem_des_content);
        txtAddUs = (TextView) findViewById(R.id.add_us_des);
        txtProblem = (TextView) findViewById(R.id.problem_des);
        imgExAddUs = (ImageView) findViewById(R.id.expand_view_add_us);
        imgExProblem = (ImageView) findViewById(R.id.expand_view_problem);
        titleBar = (EaseTitleBar) findViewById(R.id.title_bar);

        txtAddUs.setHeight(txtAddUs.getLineHeight() * maxDescripLine);
        txtProblem.setHeight(txtProblem.getLineHeight() * maxDescripLine);
        txtAddUs.post(new Runnable() {

            @Override
            public void run() {
                imgExAddUs.setVisibility(txtAddUs.getLineCount() > maxDescripLine ? View.VISIBLE : View.GONE);

            }
        });
        txtProblem.post(new Runnable() {

            @Override
            public void run() {
                imgExProblem.setVisibility(txtProblem.getLineCount() > maxDescripLine ? View.VISIBLE : View.GONE);

            }
        });
    }

    private void registEvent() {
        llytAddUsContent.setOnClickListener(this);
        llytProblemContent.setOnClickListener(this);
        titleBar.setLeftLayoutClickListener(this);
    }

    private void fullOrResetData() {
        UserDto userDto = UserPreferences.getInstance(ContactUsActivity.this).getUser();
        if (Validator.isNotEmpty(userDto)) {
            String invitor = userDto.getInviter();
            String idcardno = userDto.getCardNo();
            String bankno = userDto.getBankNo();


//            if (Validator.isNotEmpty(address)) {
//                txtAddress.setText(address);
//                edtAddress.setText(address);
//            }
//
//            if (Validator.isNotEmpty(business)) {
//                txtBusiness.setText(business);
//                edtBusiness.setText(business);
//            }
//
//            resetImage(imgPropertyDto);

        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_us_des_content:

            {
                isExpandAddUs = !isExpandAddUs;
                txtAddUs.clearAnimation();
                final int deltaValue;
                final int startValue = txtAddUs.getHeight();
                int durationMillis = 350;
                if (isExpandAddUs) {
                    deltaValue = txtAddUs.getLineHeight() * txtAddUs.getLineCount() - startValue;
                    RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    imgExAddUs.startAnimation(animation);
                } else {
                    deltaValue = txtAddUs.getLineHeight() * maxDescripLine - startValue;
                    RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    imgExAddUs.startAnimation(animation);
                }
                Animation animation = new Animation() {
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        txtAddUs.setHeight((int) (startValue + deltaValue * interpolatedTime));
                    }

                };
                animation.setDuration(durationMillis);
                txtAddUs.startAnimation(animation);
            }

            break;

            case R.id.problem_des_content: {
                isExpandProblem = !isExpandProblem;
                txtProblem.clearAnimation();
                final int deltaValue;
                final int startValue = txtProblem.getHeight();
                int durationMillis = 350;
                if (isExpandProblem) {
                    deltaValue = txtProblem.getLineHeight() * txtProblem.getLineCount() - startValue;
                    RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    imgExProblem.startAnimation(animation);
                } else {
                    deltaValue = txtProblem.getLineHeight() * maxDescripLine - startValue;
                    RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    imgExProblem.startAnimation(animation);
                }
                Animation animation = new Animation() {
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        txtProblem.setHeight((int) (startValue + deltaValue * interpolatedTime));
                    }

                };
                animation.setDuration(durationMillis);
                txtProblem.startAnimation(animation);
            }
            break;

            case R.id.left_layout:
                finish();
                break;
        }
    }
}
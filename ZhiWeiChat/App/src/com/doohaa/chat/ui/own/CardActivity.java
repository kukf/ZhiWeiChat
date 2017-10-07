package com.doohaa.chat.ui.own;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.doohaa.chat.R;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.SearchUserDto;
import com.doohaa.chat.api.dto.UserDto;
import com.doohaa.chat.preferences.UserPreferences;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.utils.ImageLoaderHelper;
import com.doohaa.chat.utils.ShareHelper;
import com.doohaa.chat.utils.Validator;

/**
 * Created by sunshixiong on 6/7/16.
 */
public class CardActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CardActivity";
    public static final String MEMBER_ID = "member_id";
    private LinearLayout llytShareContainer;
    private ImageView imgProfile;

    private TextView txtUserName;
    private TextView txtId;
    private TextView txtCompany;
    private TextView txtPosition;
    private TextView txtMobilePhone;
    private TextView txtLandLine;
    private TextView txtEmail;
    private TextView txtAddress;
    private TextView txtBusiness;

    private Button btnShare;

    private ImageLoaderHelper imageLoaderHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_card);
        initComponent();
        registEvent();
        fullOrResetData();
    }

    private void initComponent() {
        llytShareContainer = (LinearLayout) findViewById(R.id.share_container);
        imgProfile = (ImageView) findViewById(R.id.user_img);

        txtUserName = (TextView) findViewById(R.id.userName);
        txtId = (TextView) findViewById(R.id.id);
        txtCompany = (TextView) findViewById(R.id.company);
        txtPosition = (TextView) findViewById(R.id.position);
        txtMobilePhone = (TextView) findViewById(R.id.mobilephone);
        txtLandLine = (TextView) findViewById(R.id.landline);
        txtEmail = (TextView) findViewById(R.id.email);
        txtAddress = (TextView) findViewById(R.id.address);
        txtBusiness = (TextView) findViewById(R.id.business);

        btnShare = (Button) findViewById(R.id.share);

        imageLoaderHelper = new ImageLoaderHelper(CardActivity.this);
    }

    private void registEvent() {
        btnShare.setOnClickListener(this);
    }

    private void fullOrResetData() {
        String fromId = getIntent().getStringExtra(MEMBER_ID);
        String loginId = UserPreferences.getInstance(CardActivity.this).getUserId() + "";
        if (Validator.isEmpty(loginId)) {
            this.finish();
        } else {
            if (Validator.isNotEmpty(fromId)) {
                if (fromId.equals(loginId)) {
                    UserDto userDto = UserPreferences.getInstance(CardActivity.this).getUser();
                    if (Validator.isNotEmpty(userDto)) {
                        setData(userDto);
                    } else {
                        finish();
                    }
                } else {
                    fetchData(fromId);
                }
            } else {
                UserDto userDto = UserPreferences.getInstance(CardActivity.this).getUser();
                if (Validator.isNotEmpty(userDto)) {
                    setData(userDto);
                } else {
                    finish();
                }
            }
        }
    }

    private void fetchData(String userId) {
        showProgressDialog();
        Response.Listener<SearchUserDto> successListener = new Response.Listener<SearchUserDto>() {
            @Override
            public void onResponse(SearchUserDto response) {
                hideProgressDialog();
                if (Validator.isNotEmpty(response)) {
                    if (Validator.isNotEmpty(response)) {
                        UserDto userDto = response.getData();
                        if (Validator.isNotEmpty(userDto)) {
                            setData(userDto);
                        }
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
            }
        };

        ApiHelper.getUserProfile(CardActivity.this, userId, "", SearchUserDto.class, successListener, errorListener);
    }

    private void setData(UserDto userDto) {
        if (Validator.isNotEmpty(userDto)) {
            String name = userDto.getName();
            long id = userDto.getFkMemberId();
            String company = userDto.getCompany();
            String position = userDto.getPosition();
            String mobilePhone = userDto.getPhone();
            String landline = userDto.getLandline();
            String email = userDto.getEmail();
            String address = userDto.getAddress();
            String business = userDto.getBusness();

            if (Validator.isNotEmpty(name)) {
                txtUserName.setText(name);
            }

            txtId.setText(id + "");


            if (Validator.isNotEmpty(company)) {
                txtCompany.setText(company);
            }

            if (Validator.isNotEmpty(position)) {
                txtPosition.setText(position);
            }


            if (Validator.isNotEmpty(mobilePhone)) {
                txtMobilePhone.setText(mobilePhone);
            }


            if (Validator.isNotEmpty(landline)) {
                txtLandLine.setText(landline);
            }


            if (Validator.isNotEmpty(email)) {
                txtEmail.setText(email);
            }


            if (Validator.isNotEmpty(address)) {
                txtAddress.setText(address);
            }

            if (Validator.isNotEmpty(business)) {
                txtBusiness.setText(business);
            }

            imageLoaderHelper.loadUserImage(userDto, imgProfile);
        }
    }

    private void share() {
        ShareHelper shareHelper = new ShareHelper(CardActivity.this);
        shareHelper.share(llytShareContainer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                share();
                break;
        }
    }
}

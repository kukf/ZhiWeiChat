package com.doohaa.chat.ui.own;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.doohaa.chat.IMHelper;
import com.doohaa.chat.R;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.ImgPropertyDto;
import com.doohaa.chat.api.dto.ModifyUserInfoDto;
import com.doohaa.chat.api.dto.UserDto;
import com.doohaa.chat.preferences.UserPreferences;
import com.doohaa.chat.ui.EventBus.BaseEvent;
import com.doohaa.chat.ui.EventBus.EventType;
import com.doohaa.chat.ui.EventBus.ModifyUserEvent;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.ui.common.OCToast;
import com.doohaa.chat.utils.ImageLoaderHelper;
import com.doohaa.chat.utils.UIUtils;
import com.doohaa.chat.utils.Validator;
import com.hyphenate.easeui.widget.EaseTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by sunshixiong on 6/7/16.
 */
public class OwnActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llytInfoContainer;
    private LinearLayout llytModifyContainer;
    private LinearLayout llytModifyButtonContainer;

    private ImageView imgProfile;
    private ImageView edtImageProfile;

    private TextView txtUserName;
    private TextView txtId;
    private TextView txtAlipayNo;
    private TextView txtBankCardNo;
    private TextView txtBankAddress;
    private TextView txtRealName;
    private TextView txtIdCardNo;

    private TextView txtCompany;
    private TextView txtPosition;
    private TextView txtMobilePhone;
    private TextView txtLandLine;
    private TextView txtEmail;
    private TextView txtAddress;
    private TextView txtBusiness;

    private EditText edtUserName;
    private TextView txtModifyId;
    private EditText edtAlipayNo;
    private EditText edtIdCardNo;
    private EditText edtBankAddress;
    private EditText edtRealName;
    private EditText edtBankCardNo;

    private EditText edtCompany;
    private EditText edtPosition;
    private EditText edtMobilePhone;
    private EditText edtLandLine;
    private EditText edtEmail;
    private EditText edtAddress;
    private EditText edtBusiness;

    private ImageView imgSelectPic;
    private TextView txtVipDate;

    private Button btnModifyInfo;
    private Button btnSaveInfo;
    private Button btnCancel;

    private ImageLoaderHelper imageLoaderHelper;
    private ProfileImageManager profileImageManager;

    private EaseTitleBar titleBar;

    ProfileImageManager.ProfileImageListener profileImageListener = new ProfileImageManager.ProfileImageListener() {
        @Override
        public void onFileUploadSuccess(String imgPropertyId) {
            save(true, imgPropertyId);
        }

        @Override
        public void onSettingBasicImage() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_own_info);
        initComponent();
        registEvent();
        fullOrResetData();
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
        llytInfoContainer = (LinearLayout) findViewById(R.id.info_container);
        llytModifyContainer = (LinearLayout) findViewById(R.id.modify_container);
        llytModifyButtonContainer = (LinearLayout) findViewById(R.id.modify_button_container);
        imgSelectPic = (ImageView) findViewById(R.id.profile_camera);

        imgProfile = (ImageView) findViewById(R.id.user_img);
        edtImageProfile = (ImageView) findViewById(R.id.edt_user_img);

        txtUserName = (TextView) findViewById(R.id.userName);
        txtId = (TextView) findViewById(R.id.id);
        txtIdCardNo = (TextView) findViewById(R.id.idCardNo);
        txtBankCardNo = (TextView) findViewById(R.id.bankCardNo);
        txtCompany = (TextView) findViewById(R.id.company);
        txtPosition = (TextView) findViewById(R.id.position);
        txtMobilePhone = (TextView) findViewById(R.id.mobilephone);
        txtLandLine = (TextView) findViewById(R.id.landline);
        txtEmail = (TextView) findViewById(R.id.email);
        txtAddress = (TextView) findViewById(R.id.address);
        txtBusiness = (TextView) findViewById(R.id.business);
        txtAlipayNo = (TextView) findViewById(R.id.alipayAccount);
        txtBankAddress = (TextView) findViewById(R.id.bankCardAddress);
        txtRealName = (TextView) findViewById(R.id.realName);

        edtUserName = (EditText) findViewById(R.id.edt_userName);
        txtModifyId = (TextView) findViewById(R.id.modify_id);
        edtIdCardNo = (EditText) findViewById(R.id.edt_idCardNo);
        edtBankCardNo = (EditText) findViewById(R.id.edt_bankCardNo);
        edtCompany = (EditText) findViewById(R.id.edt_company);
        edtPosition = (EditText) findViewById(R.id.edt_position);
        edtMobilePhone = (EditText) findViewById(R.id.edt_mobilephone);
        edtLandLine = (EditText) findViewById(R.id.edt_landline);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtAddress = (EditText) findViewById(R.id.edt_address);
        edtBusiness = (EditText) findViewById(R.id.edt_business);
        edtAlipayNo = (EditText) findViewById(R.id.edt_alipayNo);
        edtBankAddress = (EditText) findViewById(R.id.edt_cardAddress);
        edtRealName = (EditText) findViewById(R.id.edt_readName);

        txtVipDate = (TextView) findViewById(R.id.member);

        btnModifyInfo = (Button) findViewById(R.id.modify);
        btnSaveInfo = (Button) findViewById(R.id.save);
        btnCancel = (Button) findViewById(R.id.cancel);

        titleBar = (EaseTitleBar) findViewById(R.id.title_bar);

        profileImageManager = new ProfileImageManager(this, profileImageListener);
        imageLoaderHelper = new ImageLoaderHelper(OwnActivity.this);
    }

    private void registEvent() {
        btnModifyInfo.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSaveInfo.setOnClickListener(this);
        imgSelectPic.setOnClickListener(this);
        titleBar.setLeftLayoutClickListener(this);
    }

    private void fullOrResetData() {
        UserDto userDto = UserPreferences.getInstance(OwnActivity.this).getUser();
        if (Validator.isNotEmpty(userDto)) {
            String name = userDto.getName();
            long id = userDto.getFkMemberId();
            String invitor = userDto.getInviter();
            String idcardno = userDto.getCardNo();
            String bankno = userDto.getBankNo();
            String alipyno = userDto.getAlipayName();
            String cardAddress = userDto.getCardAddress();
            String realName = userDto.getRealName();

            String company = userDto.getCompany();
            String position = userDto.getPosition();
            String mobilePhone = userDto.getPhone();
            String landline = userDto.getLandline();
            String email = userDto.getEmail();
            String address = userDto.getAddress();
            String business = userDto.getBusness();
            ImgPropertyDto imgPropertyDto = userDto.getImgProperty();

            if (Validator.isNotEmpty(name)) {
                txtUserName.setText(name);
                edtUserName.setText(name);
            }

            txtId.setText(id + "");
            txtModifyId.setText(id + "");

            if (Validator.isNotEmpty(idcardno)) {
                txtIdCardNo.setText(idcardno);
                edtIdCardNo.setText(idcardno);
            } else {
                txtIdCardNo.setText("");
                edtIdCardNo.setText("");
            }

            if (Validator.isNotEmpty(bankno)) {
                txtBankCardNo.setText(bankno);
                edtBankCardNo.setText(bankno);
            } else {
                txtBankCardNo.setText("");
                edtBankCardNo.setText("");
            }


            if (Validator.isNotEmpty(alipyno)) {
                txtAlipayNo.setText(alipyno);
                edtAlipayNo.setText(alipyno);
            } else {
                txtAlipayNo.setText("");
                edtAlipayNo.setText("");
            }

            if (Validator.isNotEmpty(cardAddress)) {
                txtBankAddress.setText(cardAddress);
                edtBankAddress.setText(cardAddress);
            } else {
                txtBankAddress.setText("");
                edtBankAddress.setText("");
            }

            if (Validator.isNotEmpty(realName)) {
                txtRealName.setText(realName);
                edtRealName.setText(realName);
            } else {
                txtRealName.setText("");
                edtRealName.setText("");
            }

            if (Validator.isNotEmpty(company)) {
                txtCompany.setText(company);
                edtCompany.setText(company);
            } else {
                txtCompany.setText("");
                edtCompany.setText("");
            }

            if (Validator.isNotEmpty(position)) {
                txtPosition.setText(position);
                edtPosition.setText(position);
            } else {
                txtPosition.setText("");
                edtPosition.setText("");
            }


            if (Validator.isNotEmpty(mobilePhone)) {
                txtMobilePhone.setText(mobilePhone);
                edtMobilePhone.setText(mobilePhone);
            } else {
                txtMobilePhone.setText("");
                edtMobilePhone.setText("");
            }


            if (Validator.isNotEmpty(landline)) {
                txtLandLine.setText(landline);
                edtLandLine.setText(landline);
            } else {
                txtLandLine.setText("");
                edtLandLine.setText("");
            }


            if (Validator.isNotEmpty(email)) {
                txtEmail.setText(email);
                edtEmail.setText(email);
            } else {
                txtEmail.setText("");
                edtEmail.setText("");
            }


            if (Validator.isNotEmpty(address)) {
                txtAddress.setText(address);
                edtAddress.setText(address);
            } else {
                txtAddress.setText("");
                edtAddress.setText("");
            }

            if (Validator.isNotEmpty(business)) {
                txtBusiness.setText(business);
                edtBusiness.setText(business);
            } else {
                txtBusiness.setText("");
                edtBusiness.setText("");
            }

            resetImage(imgPropertyDto);

        } else {
            finish();
        }
    }

    private void modify() {
        llytInfoContainer.setVisibility(View.GONE);
        llytModifyContainer.setVisibility(View.VISIBLE);

        llytModifyButtonContainer.setVisibility(View.VISIBLE);
        btnModifyInfo.setVisibility(View.GONE);
    }

    private void resetPage() {
        llytInfoContainer.setVisibility(View.VISIBLE);
        llytModifyContainer.setVisibility(View.GONE);

        llytModifyButtonContainer.setVisibility(View.GONE);
        btnModifyInfo.setVisibility(View.VISIBLE);
        fullOrResetData();
    }

    private void save(final boolean isModifyImage, String imgPropertyId) {
        showProgressDialog();
        Response.Listener<ModifyUserInfoDto> successListener = new Response.Listener<ModifyUserInfoDto>() {
            @Override
            public void onResponse(ModifyUserInfoDto response) {
                hideProgressDialog();
                if (Validator.isNotEmpty(response)) {
                    UserDto userDto = response.getData();
                    if (Validator.isNotEmpty(userDto)) {
                        UserPreferences.getInstance(OwnActivity.this).setUser(userDto);
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                IMHelper.getInstance().resetCurrentUser();
                            }
                        }.start();

                        EventBus.getDefault().post(new ModifyUserEvent(EventType.MODIFY_USER, userDto));
                        if (isModifyImage) {
                            resetImage(userDto.getImgProperty());
                        } else {
                            resetPage();
                        }
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                OCToast.makeToast(OwnActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        ApiHelper.modifyUserInfo(OwnActivity.this, isModifyImage ?

                getImageRequestUser(imgPropertyId) :

                getRequestUser(), ModifyUserInfoDto

                .class, successListener, errorListener);
    }

    private UserDto getRequestUser() {
        UserDto userDto = UserPreferences.getInstance(OwnActivity.this).getUser();
        userDto.setName(edtUserName.getText().toString().trim());
        userDto.setCardNo(edtIdCardNo.getText().toString().trim());
        userDto.setBankNo(edtBankCardNo.getText().toString().trim());

        userDto.setAlipayName(edtAlipayNo.getText().toString().trim());
        userDto.setCardAddress(edtBankAddress.getText().toString().trim());
        userDto.setRealName(edtRealName.getText().toString().trim());

        userDto.setCompany(edtCompany.getText().toString().trim());
        userDto.setPosition(edtPosition.getText().toString().trim());
        userDto.setPhone(edtMobilePhone.getText().toString().trim());
        userDto.setLandline(edtLandLine.getText().toString().trim());
        userDto.setEmail(edtEmail.getText().toString().trim());
        userDto.setAddress(edtAddress.getText().toString().trim());
        userDto.setBusness(edtBusiness.getText().toString().trim());
        return userDto;
    }

    private UserDto getImageRequestUser(String imgPropertyId) {
        UserDto userDto = UserPreferences.getInstance(OwnActivity.this).getUser();
        userDto.setFkImgPropertyId(imgPropertyId);
        return userDto;
    }

    private void selectPic() {
        profileImageManager.showPopup();
    }

    private void resetImage(ImgPropertyDto imgPropertyDto) {
        String url = "";
        if (Validator.isNotEmpty(imgPropertyDto)) {
            url = UIUtils.getImageUrl(imgPropertyDto);
        }
        imageLoaderHelper.loadImage(url, R.drawable.ease_default_avatar, imgProfile);
        imageLoaderHelper.loadImage(url, R.drawable.ease_default_avatar, edtImageProfile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify:
                modify();
                break;

            case R.id.cancel:
                resetPage();
                break;

            case R.id.save:
                save(false, null);
                break;

            case R.id.profile_camera:
                selectPic();
                break;

            case R.id.left_layout:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        profileImageManager.onActivityResult(requestCode, resultCode, data);
    }
}

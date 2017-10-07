package com.doohaa.chat.ui.own;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doohaa.chat.ui.wallet.WalletActivity;
import com.hyphenate.EMCallBack;
import com.doohaa.chat.Constant;
import com.doohaa.chat.IMHelper;
import com.doohaa.chat.R;
import com.doohaa.chat.preferences.UserPreferences;
import com.doohaa.chat.ui.EventBus.BaseEvent;
import com.doohaa.chat.ui.EventBus.ModifyUserEvent;
import com.doohaa.chat.ui.MainActivity;
import com.doohaa.chat.ui.login.LoginActivity;
import com.doohaa.chat.ui.settings.SettingsActivity;
import com.doohaa.chat.utils.ImageLoaderHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 我
 */
public class OwnFragment extends Fragment implements OnClickListener {
    private LinearLayout llytSettings;
    private LinearLayout llytRecharge;
    private LinearLayout llytWallet;
    private LinearLayout llytBussinessCard;
    private LinearLayout llytAccountInfo;
    private LinearLayout llytOwnInfo;
    private LinearLayout llytContactUs;
    private Button logoutBtn;
    private TextView txtUserName;
    private TextView txtID;

    private ImageView imgAvatar;

    private ImageLoaderHelper imageLoaderHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.doohaa.chat.R.layout.em_fragment_conversation_own, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;

        initComponent();
        registEvent();
        fillData();
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
        switch (event.getEventType()) {
            case MODIFY_USER:
                if (event instanceof ModifyUserEvent) {
                    fillData();
                }
                break;
        }
    }

    private void initComponent() {
        imageLoaderHelper = new ImageLoaderHelper(getActivity());

        llytRecharge = (LinearLayout) getView().findViewById(R.id.recharge);
        llytWallet = (LinearLayout) getView().findViewById(R.id.wallet);
        llytBussinessCard = (LinearLayout) getView().findViewById(R.id.bussiness_card);
        llytAccountInfo = (LinearLayout) getView().findViewById(R.id.account_info);
        llytOwnInfo = (LinearLayout) getView().findViewById(R.id.own_info);
        llytContactUs = (LinearLayout) getView().findViewById(R.id.contact_us);
        llytSettings = (LinearLayout) getView().findViewById(R.id.settings);
        logoutBtn = (Button) getView().findViewById(com.doohaa.chat.R.id.btn_logout);
        imgAvatar = (ImageView) getView().findViewById(R.id.avatar);

        txtUserName = (TextView) getView().findViewById(R.id.userName);
        txtID = (TextView) getView().findViewById(R.id.id);

    }

    private void registEvent() {
        llytRecharge.setOnClickListener(this);
        llytWallet.setOnClickListener(this);
        llytBussinessCard.setOnClickListener(this);
        llytAccountInfo.setOnClickListener(this);
        llytOwnInfo.setOnClickListener(this);
        llytContactUs.setOnClickListener(this);
        llytSettings.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
    }

    private void fillData() {
        txtID.setText(UserPreferences.getInstance(getActivity()).getUserId() + "");
        txtUserName.setText(UserPreferences.getInstance(getActivity()).getUserName());
        imageLoaderHelper.loadCurrentUserImage(imgAvatar);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge:
                recharge();
                break;

            case R.id.wallet:
                wallet();
                break;

            case R.id.bussiness_card:
                bCard();
                break;

            case R.id.account_info:
                accountInfo();
                break;
            case R.id.own_info:
                ownInfo();
                break;

            case R.id.contact_us:
                contactUs();
                break;
            case com.doohaa.chat.R.id.btn_logout: //退出登陆
                logout();
                break;

            case R.id.settings:
                settings();
                break;
            default:
                break;
        }
    }

    private void recharge() {
        Intent intent = new Intent(getActivity(), RechargeActivity.class);
        startActivity(intent);
    }

    private void wallet(){
        Intent intent = new Intent(getActivity(), WalletActivity.class);
        startActivity(intent);
    }

    private void bCard() {
        startActivity(new Intent(getActivity(), CardActivity.class));
    }

    private void accountInfo() {
        startActivity(new Intent(getActivity(), AccountActivity.class));
    }

    private void ownInfo() {
        startActivity(new Intent(getActivity(), OwnActivity.class));
    }

    private void contactUs() {
        startActivity(new Intent(getActivity(), ContactUsActivity.class));
    }

    private void settings() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }

    void logout() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        String st = getResources().getString(com.doohaa.chat.R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        IMHelper.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // 重新显示登陆页面
                        ((MainActivity) getActivity()).finish();
                        startActivity(new Intent(getActivity(), LoginActivity.class));

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(getActivity(), "unbind devicetokens failed", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (((MainActivity) getActivity()).isConflict) {
            outState.putBoolean("isConflict", true);
        } else if (((MainActivity) getActivity()).getCurrentAccountRemoved()) {
            outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
        }
    }
}

package com.doohaa.chat.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doohaa.chat.IMHelper;
import com.doohaa.chat.IMModel;
import com.doohaa.chat.R;
import com.doohaa.chat.ui.BlacklistActivity;
import com.doohaa.chat.ui.common.BaseActivity;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.hyphenate.easeui.widget.EaseTitleBar;

/**
 * 设置界面
 */
public class SettingsActivity extends BaseActivity implements OnClickListener {

    /**
     * 设置新消息通知布局
     */
    private RelativeLayout rl_switch_notification;
    /**
     * 设置声音布局
     */
    private RelativeLayout rl_switch_sound;
    /**
     * 设置震动布局
     */
    private RelativeLayout rl_switch_vibrate;
    /**
     * 设置扬声器布局
     */
    private RelativeLayout rl_switch_speaker;


    /**
     * 声音和震动中间的那条线
     */
    private TextView textview1, textview2;

    private LinearLayout blacklistContainer;

    private EaseSwitchButton notifiSwitch;
    private EaseSwitchButton soundSwitch;
    private EaseSwitchButton vibrateSwitch;
    private EaseSwitchButton speakerSwitch;
    private IMModel settingsModel;
    private EaseTitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_settings);

        initComponent();
        registEvent();
    }

    private void initComponent() {
        rl_switch_notification = (RelativeLayout) findViewById(com.doohaa.chat.R.id.rl_switch_notification);
        rl_switch_sound = (RelativeLayout) findViewById(com.doohaa.chat.R.id.rl_switch_sound);
        rl_switch_vibrate = (RelativeLayout) findViewById(com.doohaa.chat.R.id.rl_switch_vibrate);
        rl_switch_speaker = (RelativeLayout) findViewById(com.doohaa.chat.R.id.rl_switch_speaker);

        notifiSwitch = (EaseSwitchButton) findViewById(com.doohaa.chat.R.id.switch_notification);
        soundSwitch = (EaseSwitchButton) findViewById(com.doohaa.chat.R.id.switch_sound);
        vibrateSwitch = (EaseSwitchButton) findViewById(com.doohaa.chat.R.id.switch_vibrate);
        speakerSwitch = (EaseSwitchButton) findViewById(com.doohaa.chat.R.id.switch_speaker);

        textview1 = (TextView) findViewById(com.doohaa.chat.R.id.textview1);
        textview2 = (TextView) findViewById(com.doohaa.chat.R.id.textview2);

        blacklistContainer = (LinearLayout) findViewById(com.doohaa.chat.R.id.ll_black_list);

        titleBar = (EaseTitleBar) findViewById(R.id.title_bar);

        settingsModel = IMHelper.getInstance().getModel();
    }

    private void registEvent() {
        blacklistContainer.setOnClickListener(this);
        rl_switch_notification.setOnClickListener(this);
        rl_switch_sound.setOnClickListener(this);
        rl_switch_vibrate.setOnClickListener(this);
        rl_switch_speaker.setOnClickListener(this);
        titleBar.setLeftLayoutClickListener(this);

        // 震动和声音总开关，来消息时，是否允许此开关打开
        // the vibrate and sound notification are allowed or not?
        if (settingsModel.getSettingMsgNotification()) {
            notifiSwitch.openSwitch();
        } else {
            notifiSwitch.closeSwitch();
        }

        // 是否打开声音
        // sound notification is switched on or not?
        if (settingsModel.getSettingMsgSound()) {
            soundSwitch.openSwitch();
        } else {
            soundSwitch.closeSwitch();
        }

        // 是否打开震动
        // vibrate notification is switched on or not?
        if (settingsModel.getSettingMsgVibrate()) {
            vibrateSwitch.openSwitch();
        } else {
            vibrateSwitch.closeSwitch();
        }

        // 是否打开扬声器
        // the speaker is switched on or not?
        if (settingsModel.getSettingMsgSpeaker()) {
            speakerSwitch.openSwitch();
        } else {
            speakerSwitch.closeSwitch();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case com.doohaa.chat.R.id.rl_switch_notification:
                if (notifiSwitch.isSwitchOpen()) {
                    notifiSwitch.closeSwitch();
                    rl_switch_sound.setVisibility(View.GONE);
                    rl_switch_vibrate.setVisibility(View.GONE);
                    textview1.setVisibility(View.GONE);
                    textview2.setVisibility(View.GONE);

                    settingsModel.setSettingMsgNotification(false);
                } else {
                    notifiSwitch.openSwitch();
                    rl_switch_sound.setVisibility(View.VISIBLE);
                    rl_switch_vibrate.setVisibility(View.VISIBLE);
                    textview1.setVisibility(View.VISIBLE);
                    textview2.setVisibility(View.VISIBLE);
                    settingsModel.setSettingMsgNotification(true);
                }
                break;
            case com.doohaa.chat.R.id.rl_switch_sound:
                if (soundSwitch.isSwitchOpen()) {
                    soundSwitch.closeSwitch();
                    settingsModel.setSettingMsgSound(false);
                } else {
                    soundSwitch.openSwitch();
                    settingsModel.setSettingMsgSound(true);
                }
                break;
            case com.doohaa.chat.R.id.rl_switch_vibrate:
                if (vibrateSwitch.isSwitchOpen()) {
                    vibrateSwitch.closeSwitch();
                    settingsModel.setSettingMsgVibrate(false);
                } else {
                    vibrateSwitch.openSwitch();
                    settingsModel.setSettingMsgVibrate(true);
                }
                break;
            case com.doohaa.chat.R.id.rl_switch_speaker:
                if (speakerSwitch.isSwitchOpen()) {
                    speakerSwitch.closeSwitch();
                    settingsModel.setSettingMsgSpeaker(false);
                } else {
                    speakerSwitch.openSwitch();
                    settingsModel.setSettingMsgVibrate(true);
                }
                break;
            case com.doohaa.chat.R.id.ll_black_list:
                startActivity(new Intent(this, BlacklistActivity.class));
                break;
            default:
                break;
        }

    }
}

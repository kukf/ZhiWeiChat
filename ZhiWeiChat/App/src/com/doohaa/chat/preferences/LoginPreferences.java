package com.doohaa.chat.preferences;

import android.content.Context;

import com.doohaa.chat.utils.StringUtils;

/**
 * @author Created by yujin park on 2015-08-19.
 */
public class LoginPreferences extends BasePreferences {

    private static final String PREF = LoginPreferences.class.getSimpleName();
    private static final String LOGIN_PREF = "LOGIN_PREF";

    private static LoginPreferences instance;

    public static LoginPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new LoginPreferences(context, PREF);
        }
        return instance;
    }

    public LoginPreferences(Context context, String prefsName) {
        super(context, prefsName);
    }

    public void setLoginName(String loginUser) {
        put(LOGIN_PREF, loginUser);
    }

    public String getLoginName() {
        String loginName = (String) get(LOGIN_PREF);
        if (StringUtils.isBlank(loginName)) {
            return "";
        }

        return loginName;
    }
}

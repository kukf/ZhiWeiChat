package com.doohaa.chat.preferences;

import android.content.Context;

import com.doohaa.chat.utils.StringUtils;
import com.doohaa.chat.utils.Validator;

/**
 * @author Created by yujin park on 2015-08-19.
 */
public class TokenPreferences extends BasePreferences {

    private static final String PREF = TokenPreferences.class.getSimpleName();
    private static final String TOKEN_PREF = "TOKEN_PREF";

    private static TokenPreferences instance;

    public static TokenPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new TokenPreferences(context, PREF);
        }
        return instance;
    }

    public TokenPreferences(Context context, String prefsName) {
        super(context, prefsName);
    }

    public void setToken(String token) {

        put(TOKEN_PREF, token);
    }

    public String getToken() {
        String token = (String) get(TOKEN_PREF);
        if (StringUtils.isBlank(token)) {
            return null;
        }

        return token;
    }

    public boolean isAlreadyLogin() {
        String token = getToken();
        return Validator.isNotEmpty(token);
    }

    public void clearToken() {
        put(TOKEN_PREF, StringUtils.EMPTY);
    }
}

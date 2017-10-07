package com.doohaa.chat.preferences;

import android.content.Context;

import com.google.gson.Gson;
import com.doohaa.chat.api.dto.ImgPropertyDto;
import com.doohaa.chat.api.dto.UserDto;
import com.doohaa.chat.utils.StringUtils;
import com.doohaa.chat.utils.Validator;

/**
 * @author Created by yujin park on 2015-08-19.
 */
public class UserPreferences extends BasePreferences {

    private static final String PREF = UserPreferences.class.getSimpleName();
    private static final String USER_INFO_PREF = "USER_INFO_PREF";

    private static UserPreferences instance;

    public static UserPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new UserPreferences(context, PREF);
        }
        return instance;
    }

    public UserPreferences(Context context, String prefsName) {
        super(context, prefsName);
    }

    public void setUser(UserDto loginDto) {
        String json = new Gson().toJson(loginDto);
        put(USER_INFO_PREF, json);
    }

    public UserDto getUser() {
        String json = (String) get(USER_INFO_PREF);
        if (StringUtils.isBlank(json)) {
            return null;
        }

        return new Gson().fromJson(json, UserDto.class);
    }

    public boolean isValidUser() {
        UserDto loginDto = getUser();
        if (loginDto != null
                && Validator.isNotEmpty(loginDto.getFkMemberId()) && loginDto.getId() > 0) {
            return true;
        }
        return false;
    }

    public void resetUser() {
        put(USER_INFO_PREF, StringUtils.EMPTY);
    }

    public long getUserId() {
        if (isValidUser()) {
            return getUser().getFkMemberId();
        }

        return 0;
    }

    public String getIMUserName() {
        if (isValidUser()) {
            return String.valueOf(getUser().getFkMemberId());
        }
        return "";
    }

    public String getUserName() {
        if (isValidUser()) {
            return getUser().getName();
        }
        return "";
    }

    public String getAlipayNo() {
        if (isValidUser()) {
            return getUser().getAlipayName();
        }
        return "";
    }

    public String getBankNo() {
        if (isValidUser()) {
            return getUser().getBankNo();
        }
        return "";
    }

    public String getBankAddress() {
        if (isValidUser()) {
            return getUser().getCardAddress();
        }
        return "";
    }

    public String getIdCardNo() {
        if (isValidUser()) {
            return getUser().getCardNo();
        }
        return "";
    }

    public String getRealName() {
        if (isValidUser()) {
            return getUser().getRealName();
        }
        return "";
    }

    public String getUserAvatar() {
        String url = "";
        if (isValidUser()) {
            ImgPropertyDto imgPropertyDto = getUser().getImgProperty();
            if (Validator.isNotEmpty(imgPropertyDto)) {
                url = "http://" + imgPropertyDto.getUrl();
            }
        }
        return url;
    }

    public boolean isRealInfoValide() {
        return Validator.isNotEmpty(getAlipayNo()) && Validator.isNotEmpty(getBankNo()) && Validator.isNotEmpty(getBankAddress()) && Validator.isNotEmpty(getRealName()) && Validator.isNotEmpty(getIdCardNo());
    }
}

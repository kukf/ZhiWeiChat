package com.doohaa.chat.api.dto;

/**
 * Created by LittleBear on 2016/5/17.
 */
public class RegistDataDto {
    private String token;
    private UserDto memberProfile;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDto getMemberProfile() {
        return memberProfile;
    }

    public void setMemberProfile(UserDto memberProfile) {
        this.memberProfile = memberProfile;
    }
}

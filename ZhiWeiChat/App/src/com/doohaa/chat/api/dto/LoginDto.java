package com.doohaa.chat.api.dto;

/**
 * Created by LittleBear on 2016/5/17.
 */
public class LoginDto extends ApiResponse {
    private LoginDataDto data;

    public LoginDataDto getData() {
        return data;
    }

    public void setData(LoginDataDto data) {
        this.data = data;
    }
}

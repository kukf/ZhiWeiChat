package com.doohaa.chat.api.dto;

/**
 * Created by sunshixiong on 6/7/16.
 */
public class ModifyUserInfoDto extends ApiResponse {
    private UserDto data;

    public UserDto getData() {
        return data;
    }

    public void setData(UserDto data) {
        this.data = data;
    }
}

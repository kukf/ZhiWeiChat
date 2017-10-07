package com.doohaa.chat.api.dto;

/**
 * Created by LittleBear on 2016/5/17.
 */
public class SearchUserDto extends ApiResponse {
    private UserDto data;

    public UserDto getData() {
        return data;
    }

    public void setData(UserDto data) {
        this.data = data;
    }
}

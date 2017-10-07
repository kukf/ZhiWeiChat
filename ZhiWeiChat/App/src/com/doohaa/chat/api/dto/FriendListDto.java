package com.doohaa.chat.api.dto;

import java.util.ArrayList;

/**
 * Created by sunshixiong on 6/15/16.
 */
public class FriendListDto extends ApiResponse {
    private ArrayList<UserDto> data;

    public ArrayList<UserDto> getData() {
        return data;
    }

    public void setData(ArrayList<UserDto> data) {
        this.data = data;
    }
}

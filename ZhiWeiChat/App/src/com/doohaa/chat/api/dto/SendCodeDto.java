package com.doohaa.chat.api.dto;

/**
 * Created by LittleBear on 2016/5/18.
 */
public class SendCodeDto extends ApiResponse {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

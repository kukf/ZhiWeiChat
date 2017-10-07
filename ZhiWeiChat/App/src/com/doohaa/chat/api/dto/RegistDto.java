package com.doohaa.chat.api.dto;

/**
 * Created by LittleBear on 2016/5/17.
 */
public class RegistDto extends ApiResponse {
    private RegistDataDto data;

    public RegistDataDto getData() {
        return data;
    }

    public void setData(RegistDataDto data) {
        this.data = data;
    }
}

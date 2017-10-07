package com.doohaa.chat.api.dto;

/**
 * Created by sunshixiong on 6/17/16.
 */
public class BulletinDto extends ApiResponse {
    private Bulletin data;

    public Bulletin getData() {
        return data;
    }

    public void setData(Bulletin data) {
        this.data = data;
    }
}

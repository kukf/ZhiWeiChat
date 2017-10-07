package com.doohaa.chat.api.dto;

/**
 * Created by axiong on 2016/8/2.
 */
public class CheckVersionDto extends ApiResponse {
    private CheckVersionDataDto data;

    public CheckVersionDataDto getData() {
        return data;
    }

    public void setData(CheckVersionDataDto data) {
        this.data = data;
    }
}

package com.doohaa.chat.api.dto;

/**
 * Created by LittleBear on 2016/6/28.
 */
public class UploadImageInfoDto extends ApiResponse {
    private UploadImageInfoDataDto data;

    public UploadImageInfoDataDto getData() {
        return data;
    }

    public void setData(UploadImageInfoDataDto data) {
        this.data = data;
    }
}

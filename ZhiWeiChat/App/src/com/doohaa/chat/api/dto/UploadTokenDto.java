package com.doohaa.chat.api.dto;

/**
 * Created by LittleBear on 2016/6/28.
 */
public class UploadTokenDto extends ApiResponse {
    private UploadTokenDataDto data;

    public UploadTokenDataDto getData() {
        return data;
    }

    public void setData(UploadTokenDataDto data) {
        this.data = data;
    }
}

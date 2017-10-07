package com.doohaa.chat.api.dto;

/**
 * Created by LittleBear on 2016/9/24.
 */
public class ProduceAbleDto extends ApiResponse {
    private ProduceAbleResultDto data;

    public void setData(ProduceAbleResultDto data) {
        this.data = data;
    }

    public ProduceAbleResultDto getData() {
        return data;
    }
}

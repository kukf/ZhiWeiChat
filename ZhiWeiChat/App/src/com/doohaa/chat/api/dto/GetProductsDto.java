package com.doohaa.chat.api.dto;

/**
 * Created by LittleBear on 2016/6/14.
 */
public class GetProductsDto extends ApiResponse {
    private GetProductsDataDto data;

    public GetProductsDataDto getData() {
        return data;
    }

    public void setData(GetProductsDataDto data) {
        this.data = data;
    }
}

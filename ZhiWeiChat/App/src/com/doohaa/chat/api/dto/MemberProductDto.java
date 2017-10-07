package com.doohaa.chat.api.dto;

import java.util.ArrayList;

/**
 * Created by sunshixiong on 6/17/16.
 */
public class MemberProductDto extends ApiResponse {
    private ArrayList<MemberProductDataDto> data;

    public ArrayList<MemberProductDataDto> getData() {
        return data;
    }

    public void setData(ArrayList<MemberProductDataDto> data) {
        this.data = data;
    }
}

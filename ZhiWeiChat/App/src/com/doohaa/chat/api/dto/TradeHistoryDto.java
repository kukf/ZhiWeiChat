package com.doohaa.chat.api.dto;

import java.util.ArrayList;

/**
 * Created by sunshixiong on 6/16/16.
 */
public class TradeHistoryDto extends ApiResponse {
    private ArrayList<TradeHistory> data;

    public ArrayList<TradeHistory> getData() {
        return data;
    }

    public void setData(ArrayList<TradeHistory> data) {
        this.data = data;
    }
}

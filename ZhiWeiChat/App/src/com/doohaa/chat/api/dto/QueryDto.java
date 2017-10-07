package com.doohaa.chat.api.dto;

import java.util.ArrayList;

/**
 * Created by LittleBear on 2017/9/18.
 */

public class QueryDto {
    private ArrayList<Query> data;

    public ArrayList<Query> getData() {
        return data;
    }

    public void setData(ArrayList<Query> data) {
        this.data = data;
    }
}

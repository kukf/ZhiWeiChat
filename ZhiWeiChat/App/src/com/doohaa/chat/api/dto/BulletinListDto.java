package com.doohaa.chat.api.dto;

import java.util.ArrayList;

/**
 * Created by sunshixiong on 6/17/16.
 */
public class BulletinListDto extends ApiResponse {
    private ArrayList<Bulletin> data;

    public ArrayList<Bulletin> getData() {
        return data;
    }

    public void setData(ArrayList<Bulletin> data) {
        this.data = data;
    }
}

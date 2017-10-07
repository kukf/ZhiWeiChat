package com.doohaa.chat.model;

import java.io.Serializable;

/**
 * Created by iRichardn on 2017/9/8.
 */

public class BaseModel implements Serializable{
    private int id;//社群id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

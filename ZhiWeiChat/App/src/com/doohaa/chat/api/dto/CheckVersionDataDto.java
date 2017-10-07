package com.doohaa.chat.api.dto;

/**
 * Created by axiong on 2016/8/2.
 */
public class CheckVersionDataDto {
    private boolean type;
    private String url;

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

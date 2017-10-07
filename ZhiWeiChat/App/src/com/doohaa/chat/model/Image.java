package com.doohaa.chat.model;

/**
 * Created by iRichard on 2017/9/8.
 */

public class Image extends BaseModel {
    private int fkAdminId;
    private String keyName;
    private String name;
    private String url;
    private String createTime;
    private String refreashTime;

    public int getFkAdminId() {
        return fkAdminId;
    }

    public void setFkAdminId(int fkAdminId) {
        this.fkAdminId = fkAdminId;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRefreashTime() {
        return refreashTime;
    }

    public void setRefreashTime(String refreashTime) {
        this.refreashTime = refreashTime;
    }
}

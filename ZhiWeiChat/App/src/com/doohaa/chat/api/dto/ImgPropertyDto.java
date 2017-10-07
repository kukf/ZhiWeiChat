package com.doohaa.chat.api.dto;

/**
 * Created by sunshixiong on 6/29/16.
 */
public class ImgPropertyDto {
    private String id;
    private String fkMemberId;
    private String keyName;
    private String name;
    private String url;
    private String createTime;
    private String refreashTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFkMemberId() {
        return fkMemberId;
    }

    public void setFkMemberId(String fkMemberId) {
        this.fkMemberId = fkMemberId;
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

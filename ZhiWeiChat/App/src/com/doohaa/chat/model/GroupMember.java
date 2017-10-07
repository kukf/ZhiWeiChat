package com.doohaa.chat.model;

/**
 * Created by iRichardn on 2017/9/15.
 */

public class GroupMember extends BaseModel {
    private long createTime;
    private int fkMemberId;
    private String keyName;
    private String name;
    private long refreashTime;
    private String url;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getFkMemberId() {
        return fkMemberId;
    }

    public void setFkMemberId(int fkMemberId) {
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

    public long getRefreashTime() {
        return refreashTime;
    }

    public void setRefreashTime(long refreashTime) {
        this.refreashTime = refreashTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

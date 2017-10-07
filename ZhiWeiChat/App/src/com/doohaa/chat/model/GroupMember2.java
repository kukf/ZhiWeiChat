package com.doohaa.chat.model;

/**
 * Created by iRichardn on 2017/9/15.
 */

public class GroupMember2 extends BaseModel {
    private long createTime;
    private int fkMemberId;
    private int imId;
    private String name;
    private int type;
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

    public int getImId() {
        return imId;
    }

    public void setImId(int imId) {
        this.imId = imId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

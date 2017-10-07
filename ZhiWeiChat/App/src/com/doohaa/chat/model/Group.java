package com.doohaa.chat.model;

/**
 * Created by iRichard on 2017/9/8.
 */

public class Group extends BaseModel{
    private String name;//社群名称
    private String certification;//认证
    private String description;//描述
    private int state;//社群状态
    private String ownerPhone;//一级管理员手机号
    private String createTime;//创建时间
    private int memberCount;//会员数
    private int noteCount;//帖子数
    private boolean isIn;
    private String imgProperty;//图片
    private Image image;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getNoteCount() {
        return noteCount;
    }

    public void setNoteCount(int noteCount) {
        this.noteCount = noteCount;
    }

    public String getImgProperty() {
        return imgProperty;
    }

    public void setImgProperty(String imgProperty) {
        this.imgProperty = imgProperty;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isIn() {
        return isIn;
    }

    public void setIn(boolean in) {
        isIn = in;
    }
}

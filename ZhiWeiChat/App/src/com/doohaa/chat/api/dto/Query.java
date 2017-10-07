package com.doohaa.chat.api.dto;

/**
 * Created by LittleBear on 2017/9/18.
 */

public class Query {
    private long id;
    private String name;
    private String certification;
    private String description;
    private int state;
    private String ownerPhone;
    private String createTime;
    private int memberCount;
    private int noteCount;
    private ImgPropertyDto imgProperty;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public ImgPropertyDto getImgProperty() {
        return imgProperty;
    }

    public void setImgProperty(ImgPropertyDto imgProperty) {
        this.imgProperty = imgProperty;
    }
}

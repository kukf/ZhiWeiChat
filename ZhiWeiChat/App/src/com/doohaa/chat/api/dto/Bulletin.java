package com.doohaa.chat.api.dto;

/**
 * Created by LittleBear on 2016/6/16.
 */
public class Bulletin {
    private int id;
    private String title;
    private String body;
    private String createTime;
    private int fkAdminId;
    private int fkImgPropertyId;
    private ImgPropertyDto imgProperty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getFkAdminId() {
        return fkAdminId;
    }

    public void setFkAdminId(int fkAdminId) {
        this.fkAdminId = fkAdminId;
    }

    public int getFkImgPropertyId() {
        return fkImgPropertyId;
    }

    public void setFkImgPropertyId(int fkImgPropertyId) {
        this.fkImgPropertyId = fkImgPropertyId;
    }

    public ImgPropertyDto getImgProperty() {
        return imgProperty;
    }

    public void setImgProperty(ImgPropertyDto imgProperty) {
        this.imgProperty = imgProperty;
    }
}

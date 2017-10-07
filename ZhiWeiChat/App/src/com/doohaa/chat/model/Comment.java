package com.doohaa.chat.model;

import java.util.List;

/**
 * Created by iRichardn on 2017/9/13.
 */

public class Comment extends BaseModel {
    private int fkNoteId;//只有回帖才有这条属性
    private String message;//消息
    private int fkGroupId;//所属社群id
    private int fkMemberId;//创建用户id
    private int type;//0帖子，1回帖
    private String createTime;//创建时间
    private String owner;//所属用户名称
    private List<Image> imageList;//帖子图片
    private int upCount;//点赞数量
    private boolean isUp;//当前用户是否已经点赞
    private List<NoteMessage> messageList;//评论内容

    public int getFkNoteId() {
        return fkNoteId;
    }

    public void setFkNoteId(int fkNoteId) {
        this.fkNoteId = fkNoteId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFkGroupId() {
        return fkGroupId;
    }

    public void setFkGroupId(int fkGroupId) {
        this.fkGroupId = fkGroupId;
    }

    public int getFkMemberId() {
        return fkMemberId;
    }

    public void setFkMemberId(int fkMemberId) {
        this.fkMemberId = fkMemberId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public int getUpCount() {
        return upCount;
    }

    public void setUpCount(int upCount) {
        this.upCount = upCount;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public List<NoteMessage> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<NoteMessage> messageList) {
        this.messageList = messageList;
    }
}

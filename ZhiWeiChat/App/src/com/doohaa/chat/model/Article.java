package com.doohaa.chat.model;

import java.util.List;

/**
 * Created by iRichard on 2017/9/11.
 */

public class Article extends BaseModel {
    private String avatar;//头像
    private int fkNoteId;//主贴id	只有回帖才有这条属性
    private String message;//消息
    private int fkGroupId;//所属社群id
    private String fkGroupName;//所属社群
    private int fkMemberId;//创建用户id
    private int type;//0帖子，1回帖
    private long createTime;//创建时间
    private long refreashTime;//刷新时间
    private String owner;//所属用户名称
    private List<Image> imageList;//帖子图片
    private int replayCount;//回复数量
    private int upCount;//点赞数量
    private boolean isUp;//当前用户是否已经点赞
    private boolean isTop;//当前帖子是否置顶
    private String  memberName;
    private boolean  isOwner;//是否是当前用户评论的
    private List<NoteMessage> notes;
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
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

    public void setIsUp(boolean isUp) {
        this.isUp = isUp;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public List<NoteMessage> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteMessage> notes) {
        this.notes = notes;
    }

    public int getReplayCount() {
        return replayCount;
    }

    public void setReplayCount(int replayCount) {
        this.replayCount = replayCount;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public String getFkGroupName() {
        return fkGroupName;
    }

    public void setFkGroupName(String fkGroupName) {
        this.fkGroupName = fkGroupName;
    }

    public long getRefreashTime() {
        return refreashTime;
    }

    public void setRefreashTime(long refreashTime) {
        this.refreashTime = refreashTime;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }
}

package com.doohaa.chat.model;

import java.util.List;

/**
 * Created by iRichard on 2017/9/12.
 */

public class NoteMessage extends BaseModel {
    private int fkNoteId;//评论帖子id
    private int fkMemberId;//用户id
    private String message;//评论内容
    private String memberName;//评论人
    private long createTime;//评论时间
    private List<NoteMessage> msgs;

    public int getFkNoteId() {
        return fkNoteId;
    }

    public void setFkNoteId(int fkNoteId) {
        this.fkNoteId = fkNoteId;
    }

    public int getFkMemberId() {
        return fkMemberId;
    }

    public void setFkMemberId(int fkMemberId) {
        this.fkMemberId = fkMemberId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public List<NoteMessage> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<NoteMessage> msgs) {
        this.msgs = msgs;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}

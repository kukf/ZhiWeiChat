package com.doohaa.chat.api.dto;

/**
 * Created by sunshixiong on 6/13/16.
 */
public class MemberFriend {
    private long id;
    private long memberId;
    private long friendId;
    private int flag;
    private String message;
    private String time;
    private UserDto memberProfile;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserDto getMemberProfile() {
        return memberProfile;
    }

    public void setMemberProfile(UserDto memberProfile) {
        this.memberProfile = memberProfile;
    }
}

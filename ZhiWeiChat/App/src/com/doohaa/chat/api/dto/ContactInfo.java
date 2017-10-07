package com.doohaa.chat.api.dto;

/**
 * Created by sunshixiong on 5/23/16.
 */
public class ContactInfo {
    private String name;
    private String number;
    private boolean isRegisted;
    private UserDto user;

    public ContactInfo(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isRegisted() {
        return isRegisted;
    }

    public void setRegisted(boolean registed) {
        isRegisted = registed;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}

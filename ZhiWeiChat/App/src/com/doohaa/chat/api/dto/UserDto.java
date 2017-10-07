package com.doohaa.chat.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by LittleBear on 2016/5/17.
 */
public class UserDto implements Serializable {
    private long id;
    private long fkMemberId;
    private String name;
    private String inviter;
    private String cardNo;
    private String bankNo;
    private String realName;
    private String cardAddress;
    private String alipayName;
    private String company;
    private String position;
    private String phone;
    private String landline;
    private String email;
    private String address;
    private String busness;
    private String imId;
    private BigDecimal money;
    private String fkImgPropertyId;
    private ImgPropertyDto imgProperty;
    private long createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFkMemberId() {
        return fkMemberId;
    }

    public void setFkMemberId(long fkMemberId) {
        this.fkMemberId = fkMemberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusness() {
        return busness;
    }

    public void setBusness(String busness) {
        this.busness = busness;
    }

    public String getImId() {
        return imId;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getFkImgPropertyId() {
        return fkImgPropertyId;
    }

    public void setFkImgPropertyId(String fkImgPropertyId) {
        this.fkImgPropertyId = fkImgPropertyId;
    }

    public ImgPropertyDto getImgProperty() {
        return imgProperty;
    }

    public void setImgProperty(ImgPropertyDto imgProperty) {
        this.imgProperty = imgProperty;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCardAddress() {
        return cardAddress;
    }

    public void setCardAddress(String cardAddress) {
        this.cardAddress = cardAddress;
    }

    public String getAlipayName() {
        return alipayName;
    }

    public void setAlipayName(String alipayName) {
        this.alipayName = alipayName;
    }
}

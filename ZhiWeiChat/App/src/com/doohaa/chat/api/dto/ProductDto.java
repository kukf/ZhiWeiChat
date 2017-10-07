package com.doohaa.chat.api.dto;

import java.math.BigDecimal;

/**
 * Created by LittleBear on 2016/6/14.
 */
public class ProductDto {
    private long id;
    private String name;
    private boolean state;
    private BigDecimal price;
    private long updateTime;
    private ProductAmountDto productAmount;

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

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public ProductAmountDto getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(ProductAmountDto productAmount) {
        this.productAmount = productAmount;
    }
}

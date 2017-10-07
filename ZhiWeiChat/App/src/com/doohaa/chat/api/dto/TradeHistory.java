package com.doohaa.chat.api.dto;

import java.math.BigDecimal;

/**
 * Created by sunshixiong on 6/16/16.
 */
public class TradeHistory {
    private int type;
    private ProductDto product;
    private int quantity;
    private long dealTime;
    private BigDecimal price;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getDealTime() {
        return dealTime;
    }

    public void setDealTime(long dealTime) {
        this.dealTime = dealTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

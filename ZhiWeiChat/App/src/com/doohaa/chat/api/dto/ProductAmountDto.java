package com.doohaa.chat.api.dto;

/**
 * Created by LittleBear on 2016/6/14.
 */
public class ProductAmountDto {
    private long id;
    private long fkProductId;
    private int quantity;
    private int sellQuantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFkProductId() {
        return fkProductId;
    }

    public void setFkProductId(long fkProductId) {
        this.fkProductId = fkProductId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSellQuantity() {
        return sellQuantity;
    }

    public void setSellQuantity(int sellQuantity) {
        this.sellQuantity = sellQuantity;
    }
}

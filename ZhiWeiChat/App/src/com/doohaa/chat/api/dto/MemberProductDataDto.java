package com.doohaa.chat.api.dto;

/**
 * Created by sunshixiong on 6/17/16.
 */
public class MemberProductDataDto {
    private long quantity;
    private ProductDto product;

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }
}

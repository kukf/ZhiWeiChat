package com.doohaa.chat.api.dto;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by LittleBear on 2016/6/14.
 */
public class GetProductsDataDto {
    private ArrayList<ProductDto> products;
    private Map<String, Integer> memberProducts;

    public ArrayList<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductDto> products) {
        this.products = products;
    }

    public Map<String, Integer> getMemberProducts() {
        return memberProducts;
    }

    public void setMemberProducts(Map<String, Integer> memberProducts) {
        this.memberProducts = memberProducts;
    }
}

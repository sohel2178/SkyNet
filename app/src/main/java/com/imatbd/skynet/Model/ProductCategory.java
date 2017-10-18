package com.imatbd.skynet.Model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

/**
 * Created by Genius 03 on 10/18/2017.
 */

public class ProductCategory implements ParentObject {

    private List<Object> productList;
    private String category;

    public ProductCategory(String category) {
        this.category = category;
    }

    @Override
    public List<Object> getChildObjectList() {
        return productList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        this.productList = list;
    }

    public void addProduct(Product product){
        productList.add(product);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

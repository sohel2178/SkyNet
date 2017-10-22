package com.imatbd.skynet.Model;


import android.content.Context;

import com.imatbd.skynet.Adapter.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Genius 03 on 10/18/2017.
 */

public class ProductCategory {

    private List<Product> productList;
    private String category;
    private boolean isExpand;
    private ProductAdapter productAdapter;


    public ProductCategory(Product product, Context context) {
        this.category = product.getCategory();
        productList = new ArrayList<>();
        productList.add(product);
        this.isExpand = false;
        this.productAdapter = new ProductAdapter(context,productList);
    }


    public List<Product> getProductList() {
        return productList;
    }


    public void addProduct(Product product){
        this.productAdapter.addProduct(product);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public ProductAdapter getProductAdapter() {
        return productAdapter;
    }

    public void setProductAdapter(ProductAdapter productAdapter) {
        this.productAdapter = productAdapter;
    }
}

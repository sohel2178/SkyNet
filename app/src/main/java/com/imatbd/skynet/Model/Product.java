package com.imatbd.skynet.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Genius 03 on 9/25/2017.
 */

public class Product implements Serializable {
    private String id;
    private String name;
    private String category;
    private double price;
    private String desciption;
    private String imageUrl;
    private String adminId;
    private int availableQuantity;
    private long createdTime;

    public Product() {
    }

    public Product(String id, String name,String category, double price, String desciption,
                   String imageUrl, String ownerId,int availableQuantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.desciption = desciption;
        this.imageUrl = imageUrl;
        this.adminId = ownerId;
        this.availableQuantity = availableQuantity;
        this.createdTime = System.currentTimeMillis();
    }

    public Product(String name,String category,String desciption,double price,String ownerId){
        this("",name,category,price,desciption,"",ownerId,0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public void addQuantity(int quantity){
        this.availableQuantity = this.availableQuantity+quantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

}

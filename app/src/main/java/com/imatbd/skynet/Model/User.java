package com.imatbd.skynet.Model;

import java.io.Serializable;

/**
 * Created by Genius 03 on 9/25/2017.
 */

public class User implements Serializable {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String shopName;
    private String address;

    private String imageUrl;
    private String token;
    private int userType;
    private String adminId;
    private String agentId;
    private long createdDate;

    public User() {
    }

    public User(String id, String name,String email,String phone,String shopName,String address,
                String imageUrl, String token, int userType, String adminId, String agentId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.shopName = shopName;
        this.address = address;
        this.imageUrl = imageUrl;
        this.token = token;
        this.userType = userType;
        this.adminId = adminId;
        this.agentId = agentId;
        this.createdDate = System.currentTimeMillis();
    }

    public User(String id, String name, String imageUrl, String token, int userType) {
        this(id,name,"","","","",imageUrl,token,userType,"","");
    }

    public User(String name,String email,String phone,int userType,String adminId){
        this("",name,email,phone,"","","","",userType,adminId,"");
    }

    public User(String name,String email,String phone,String shopName,String address,int userType,String adminId,String agentId){
        this("",name,email,phone,shopName,address,"","",userType,adminId,agentId);
    }

    public User(String name,int userType){
        this("",name,"","",userType);
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

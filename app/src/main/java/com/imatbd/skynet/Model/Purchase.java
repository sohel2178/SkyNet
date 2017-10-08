package com.imatbd.skynet.Model;

import java.io.Serializable;

/**
 * Created by Genius 03 on 10/8/2017.
 */

public class Purchase implements Serializable {
    private String id;
    private String product_id;
    private String purchase_from;
    private String admin_id;
    private double unit_price;
    private int quantity;
    private long purchase_time;

    public Purchase() {
    }

    public Purchase(String id, String product_id, String admin_id, String purchase_from,
                    double unit_price, int quantity) {
        this.id = id;
        this.product_id = product_id;
        this.admin_id = admin_id;
        this.purchase_from = purchase_from;
        this.unit_price = unit_price;
        this.quantity = quantity;
        this.purchase_time = System.currentTimeMillis();
    }

    public Purchase( String product_id, String purchase_from,String admin_id,
                    double unit_price, int quantity) {
        this("",product_id,purchase_from,admin_id,unit_price,quantity);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPurchase_from() {
        return purchase_from;
    }

    public void setPurchase_from(String purchase_from) {
        this.purchase_from = purchase_from;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPurchase_time() {
        return purchase_time;
    }

    public void setPurchase_time(long purchase_time) {
        this.purchase_time = purchase_time;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }
}

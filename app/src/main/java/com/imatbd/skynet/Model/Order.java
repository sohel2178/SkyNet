package com.imatbd.skynet.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Genius 03 on 10/10/2017.
 */

public class Order implements Serializable{

    private String order_id;
    private int order_state;
    private List<CartItem> cart_item_list;
    private String customerId;
    private String agentId;
    private String adminId;


    public Order() {
    }

    public Order(String order_id, int order_state, List<CartItem> cart_item_list, String customerId, String agentId, String ownerId) {
        this.order_id = order_id;
        this.order_state = order_state;
        this.cart_item_list = cart_item_list;
        this.customerId = customerId;
        this.agentId = agentId;
        this.adminId = ownerId;
    }

    public Order(int order_state, List<CartItem> cart_item_list, String customerId, String agentId, String ownerId) {
        this("",order_state,cart_item_list,customerId,agentId,ownerId);
    }


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getOrder_state() {
        return order_state;
    }

    public void setOrder_state(int order_state) {
        this.order_state = order_state;
    }

    public List<CartItem> getCart_item_list() {
        return cart_item_list;
    }

    public void setCart_item_list(List<CartItem> cart_item_list) {
        this.cart_item_list = cart_item_list;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}

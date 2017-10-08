package com.imatbd.skynet.Model;

import java.io.Serializable;

/**
 * Created by Genius 03 on 10/2/2017.
 */

public class NavData implements Serializable {

    private int id;
    private String name;
    private int resId;

    public NavData() {
    }

    public NavData(int id, String name, int resId) {
        this.id = id;
        this.name = name;
        this.resId = resId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}

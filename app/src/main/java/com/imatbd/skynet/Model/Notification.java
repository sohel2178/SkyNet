package com.imatbd.skynet.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Genius 03 on 7/22/2017.
 */

public class Notification {
    private String body;
    private String title;
    private String icon;
    private String sound;

    public Notification(String body, String title, String icon) {
        this.body = body;
        this.title = title;
        this.icon = icon;
        this.sound = "default";
    }

    public Notification(String body, String title) {
        this.body = body;
        this.title = title;
        this.sound = "default";
    }

    public Notification() {
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public JSONObject getNotificationObject(){
        JSONObject object = new JSONObject();
        try {
            object.put("title",title);
            object.put("body",body);
            object.put("sound",sound);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        try {
            object.put("title",title);
            object.put("body",body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}

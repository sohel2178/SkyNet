package com.imatbd.skynet.AppUtility;

import android.content.Context;
import android.content.SharedPreferences;

import com.imatbd.skynet.Model.User;

/**
 * Created by Genius 03 on 9/27/2017.
 */

public class UserData {
    private static final String SP_NAME ="userData";

    SharedPreferences userLocalDatabase;

    private static UserData userData = null;

    private UserData(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    public static UserData getInstance(Context context){
        if(userData==null){
            userData= new UserData(context);
        }

        return userData;
    }

    public void saveUser(User user){

        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("user_id",user.getId());
        spEditor.putString("user_name",user.getName());
        spEditor.putString("email",user.getEmail());
        spEditor.putString("phone",user.getPhone());
        spEditor.putString("shop_name",user.getShopName());
        spEditor.putString("address",user.getAddress());
        spEditor.putString("user_photo",user.getImageUrl());
        spEditor.putString("user_token",user.getToken());
        spEditor.putInt("user_type",user.getUserType());
        spEditor.putString("admin_id",user.getAdminId());
        spEditor.putString("agent_id",user.getAgentId());

        //spEditor.putBoolean("loginstatus",false);
        spEditor.apply();

    }

    public User getUser(){
        String user_id = userLocalDatabase.getString("user_id","");
        String user_name = userLocalDatabase.getString("user_name","");
        String email = userLocalDatabase.getString("email","");
        String phone = userLocalDatabase.getString("phone","");
        String shopName = userLocalDatabase.getString("shop_name","");
        String address = userLocalDatabase.getString("address","");
        String user_photo = userLocalDatabase.getString("user_photo","");
        String token = userLocalDatabase.getString("token","");
        int user_type = userLocalDatabase.getInt("user_type",-1);
        String adminID = userLocalDatabase.getString("admin_id","");
        String agentId = userLocalDatabase.getString("agent_id","");

        User user = new User(user_id,user_name,email,shopName,address,phone,user_photo,token,user_type,adminID,agentId);

        return user;
    }

    public void setImageUrl(String url){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();

        spEditor.putString("user_photo",url);
        spEditor.apply();
    }
}

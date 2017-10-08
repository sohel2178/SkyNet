package com.imatbd.skynet.AppUtility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Genius 03 on 7/13/2017.
 */

public class UserLocalStore {
    private static final String SP_NAME ="userDetail";
    private static final String USER_NAME="USER_NAME";
    private static final String USER_ID="USER_ID";
    private static final String IS_SYNC="IS_SYNC";
    private static final String TOKEN="TOKEN";
    private static final String IS_REGISTER="IS_REGISTER";
    private static final String USER_TYPE="USER_TYPE";
    private static final String PROVIDER_ENABLE="PROVIDER_ENABLE";


    SharedPreferences userLocalDatabase;

    private static UserLocalStore userLocalStore = null;

    private UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    public static UserLocalStore getInstance(Context context){
        if(userLocalStore==null){
            userLocalStore= new UserLocalStore(context);
        }

        return userLocalStore;
    }

    public void setUserName(String userName){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString(USER_NAME,userName);
        spEditor.apply();

    }


    public String getUserName(){
        return userLocalDatabase.getString(USER_NAME,"");
    }

    public void setUserId(String userId){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString(USER_ID,userId);
        spEditor.apply();

    }


    public String getUserId(){
        return userLocalDatabase.getString(USER_ID,null);
    }

    public void setSync(boolean value){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean(IS_SYNC,value);
        spEditor.apply();

    }

    public void setToken(String token){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString(TOKEN,token);
        spEditor.apply();

    }

    public String getToken(){
        return userLocalDatabase.getString(TOKEN,null);
    }

    public void setRegister(boolean value){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean(IS_REGISTER,value);
        spEditor.apply();

    }

    public boolean isRegistered(){
        return userLocalDatabase.getBoolean(IS_REGISTER,false);
    }

    public void setProviderEnable(boolean value){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean(PROVIDER_ENABLE,value);
        spEditor.apply();

    }

    public boolean getProviderEnable(){
        return userLocalDatabase.getBoolean(PROVIDER_ENABLE,false);
    }

    public void setUserType(int value){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putInt(USER_TYPE,value);
        spEditor.apply();

    }

    public int getUserType(){
        return userLocalDatabase.getInt(USER_TYPE,-1);
    }




    public boolean getSync(){
        return userLocalDatabase.getBoolean(IS_SYNC,false);
    }
}

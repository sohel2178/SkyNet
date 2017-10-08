package com.imatbd.skynet.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.imatbd.skynet.AppUtility.DialogHelper;
import com.imatbd.skynet.Firebase.FdatabaseUtil;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.Utility.Constant;
import com.imatbd.skynet.Utility.Method;

/**
 * Created by Genius 03 on 9/26/2017.
 */

public class DatabaseCommunicationService extends IntentService implements FdatabaseUtil.UserListener {
    private static final String TAG = DatabaseCommunicationService.class.getSimpleName();

    private FdatabaseUtil fdatabaseUtil;

    private ResultReceiver resultReceiver;

    public DatabaseCommunicationService() {
        super("My Background Thread");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG,"Oncreate is Called");
        Log.d(TAG,Thread.currentThread().getName());

        fdatabaseUtil = new FdatabaseUtil(getApplicationContext());
        fdatabaseUtil.setListener(this);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG,"onHandleIntent is Called");
        Log.d(TAG,Thread.currentThread().getName());



        int methodId = intent.getIntExtra(Constant.METHOD_REQ,-1);
        resultReceiver = intent.getParcelableExtra(Constant.RECEIVER);

        if(methodId!=-1){
            switch (methodId){
                case Method.ADD_ADMIN:
                    fdatabaseUtil.addAdmin(new User("Sajib Biswas",1));
                    resultReceiver.send(Constant.SERVICE_REQ_CODE,new Bundle());
                    break;

                case Method.FETCH_USER:
                    String code = intent.getStringExtra("code");
                    fdatabaseUtil.getUserByID(code);
                    break;

                case Method.UPDATE_TOKEN:
                    User user = (User) intent.getSerializableExtra(Constant.USER);
                    if(user!=null){
                        fdatabaseUtil.updateToken(user);
                    }
                    break;
            }
        }








    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy is Called");
        Log.d(TAG,Thread.currentThread().getName());
        super.onDestroy();
    }


    @Override
    public void getUser(User user,int methodId) {

        switch (methodId){

            case Method.FETCH_USER:
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
                bundle.putInt(Constant.METHOD_REQ,methodId);
                resultReceiver.send(Constant.SERVICE_REQ_CODE,bundle);
                break;

            case Method.UPDATE_TOKEN:
                Bundle updateTokenBundle = new Bundle();
                updateTokenBundle.putInt(Constant.METHOD_REQ,methodId);
                resultReceiver.send(Constant.SERVICE_REQ_CODE,updateTokenBundle);
                break;
        }



    }
}

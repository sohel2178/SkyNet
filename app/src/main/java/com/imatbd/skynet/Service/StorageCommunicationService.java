package com.imatbd.skynet.Service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.imatbd.skynet.AppUtility.MyUtils;
import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.Firebase.FdatabaseUtil;
import com.imatbd.skynet.FirebaseStorage.FirebaseStorageUtil;
import com.imatbd.skynet.Utility.Constant;
import com.imatbd.skynet.Utility.Method;

/**
 * Created by Genius 03 on 9/28/2017.
 */

public class StorageCommunicationService extends IntentService implements FirebaseStorageUtil.StorageListener {

    private FirebaseStorageUtil firebaseStorageUtil;
    private FdatabaseUtil fdatabaseUtil;

    private ResultReceiver resultReceiver;

    public StorageCommunicationService() {
        super("StorageCommunicationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseStorageUtil = new FirebaseStorageUtil(getApplicationContext());
        firebaseStorageUtil.setListener(this);

        fdatabaseUtil = new FdatabaseUtil(getApplicationContext());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        int methodId = intent.getIntExtra(Constant.METHOD_REQ,-1);
        String id = intent.getStringExtra(Constant.ID);


        resultReceiver = intent.getParcelableExtra(Constant.RECEIVER);
        String path = intent.getStringExtra(Constant.PATH);


        Bitmap bitmap = MyUtils.resizeBitmap(path,150,150);


        if(methodId!=-1){
            switch (methodId){

                case Method.UPLOAD_IMAGE:
                    if(bitmap!=null && id !=null){
                        firebaseStorageUtil.uploadImage(bitmap,id);
                    }

                    break;

                case Method.UPLOAD_PRODUCT_IMAGE:

                    if(bitmap!=null && id !=null){
                        firebaseStorageUtil.uploadProductImage(bitmap,id);
                    }

                    break;
            }
        }

    }

    @Override
    public void getImageUrl(String url,String id ,int methodId) {

        switch (methodId){
            case Method.UPLOAD_IMAGE:

                // Upload Image to the Data base
                fdatabaseUtil.updateProfileImage(url);
                // Save image url in Locally
                UserData.getInstance(getApplicationContext()).setImageUrl(url);

                // Broadcast Result to Activity
                Bundle bundle = new Bundle();
                bundle.putString(Constant.URL,url);
                bundle.putInt(Constant.METHOD_REQ,methodId);

                resultReceiver.send(Constant.SERVICE_REQ_CODE,bundle);

                break;

            case Method.UPLOAD_PRODUCT_IMAGE:
                // Upload Image to the Data base
                fdatabaseUtil.updateProductImage(url,id);

                // Broadcast Result to Activity
                Bundle prodBundle = new Bundle();
                prodBundle.putString(Constant.URL,url);
                prodBundle.putInt(Constant.METHOD_REQ,methodId);

                resultReceiver.send(Constant.SERVICE_REQ_CODE,prodBundle);
                break;
        }


    }
}

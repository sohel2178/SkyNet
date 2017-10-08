package com.imatbd.skynet.FirebaseStorage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.imatbd.skynet.Utility.Constant;
import com.imatbd.skynet.Utility.Method;

import java.io.ByteArrayOutputStream;

/**
 * Created by Genius 03 on 9/28/2017.
 */

public class FirebaseStorageUtil implements OnSuccessListener{

    private MyFirebaseStorage myFirebaseStorage;
    private Context context;
    private StorageListener listener;

    public FirebaseStorageUtil(Context context) {
        this.context = context;
        myFirebaseStorage = new MyFirebaseStorage();
    }

    public void setListener(StorageListener listener){
        this.listener = listener;
    }


    public void uploadImage(Bitmap bitmap,String id){

        Log.d("SOHEL","Inside Firebase Method");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask =myFirebaseStorage.getUserImageReference().child(id).putBytes(data);
        uploadTask.addOnSuccessListener(this);

    }

    public void uploadProductImage(Bitmap bitmap,String id){
        Log.d("HHH","From uploadProductImage Mthod");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask =myFirebaseStorage.getProductReference().child(id).putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String url = String.valueOf(taskSnapshot.getDownloadUrl());
                String id = taskSnapshot.getMetadata().getName();

                Log.d("MMM","Id "+id);
                Log.d("MMM","Id "+id);
                Log.d("MMM","Id "+Method.UPLOAD_PRODUCT_IMAGE);

                if(listener!=null){
                    listener.getImageUrl(url,id, Method.UPLOAD_PRODUCT_IMAGE);

                    Log.d("MMM","Id "+id);
                    Log.d("MMM","Id "+id);
                    Log.d("MMM","Id "+Method.UPLOAD_PRODUCT_IMAGE);
                }
            }
        });
    }

    @Override
    public void onSuccess(Object o) {

        Log.d("SOHEL","Upload Image Call");

        UploadTask.TaskSnapshot snapshot = (UploadTask.TaskSnapshot) o;
        String url = String.valueOf(snapshot.getDownloadUrl());

        String id = snapshot.getMetadata().getName();


        if(listener!=null){
            listener.getImageUrl(url,id, Method.UPLOAD_IMAGE);
        }

    }

    public interface StorageListener{
        public void getImageUrl(String url,String id ,int methodId);
    }
}

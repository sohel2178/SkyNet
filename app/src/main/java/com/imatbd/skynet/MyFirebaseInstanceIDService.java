package com.imatbd.skynet;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.imatbd.skynet.AppUtility.UserLocalStore;
import com.imatbd.skynet.Firebase.MyDatabaseReference;

/**
 * Created by Genius 03 on 7/22/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private UserLocalStore userLocalStore;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        //Save it in Local Data Store
        UserLocalStore.getInstance(getApplicationContext()).setToken(refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

        userLocalStore = UserLocalStore.getInstance(getApplicationContext());

        if(userLocalStore.getUserId()!=null){
            MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
            myDatabaseReference.getUserRef().child(userLocalStore.getUserId()).child("token").setValue(token);
        }


    }
}

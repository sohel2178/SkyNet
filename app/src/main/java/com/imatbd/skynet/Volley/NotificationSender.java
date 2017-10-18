package com.imatbd.skynet.Volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.imatbd.skynet.Firebase.MyDatabaseReference;
import com.imatbd.skynet.Model.Notification;
import com.imatbd.skynet.Utility.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Genius 03 on 7/22/2017.
 */

public class NotificationSender {
    private static final String URL="https://fcm.googleapis.com/fcm/send";
    private static final String AUTHORIZATION_KEY="key=AIzaSyD5JolpYzJyM9VtX2yDT-0eKg4mZ7ZURuo";
    private static final String CONTENT_TYPE="application/json";

    private Context context;

    public NotificationSender(Context context) {
        this.context = context;
    }

    public void sendNotification(Notification notification, String to_token){
        JSONObject object = getNotificationObject(notification,to_token);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL,object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success

                Log.d("RESPONSE",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //TODO: handle failure
                Log.d("ERROR",error.toString());
            }
        }){



            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", CONTENT_TYPE);
                headers.put("Authorization", AUTHORIZATION_KEY);
                return headers;
            }

        };
        Volley.newRequestQueue(context).add(jsonRequest);



    }

    private JSONObject getNotificationObject(Notification notification,String token){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("to",token);
            jsonObject.put("notification",notification.getNotificationObject());

            JSONObject data = new JSONObject();
            data.put("bal","BALLL");
            data.put("sal","Sallll");

            jsonObject.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("HHHH",jsonObject.toString());

        return jsonObject;
    }

    private void sendNoti(String orderId,String token){

        JSONObject mainJsonObject = new JSONObject();

        JSONObject notificationObject = new JSONObject();
        JSONObject dataObject = new JSONObject();
        try {
            notificationObject.put("title","Order Request");
            notificationObject.put("body","You Have a New Order from a Customer");
            notificationObject.put("sound","default");

            dataObject.put(Constant.ORDER_ID,orderId);

            mainJsonObject.put("to",token);
            mainJsonObject.put("notification",notificationObject);
            mainJsonObject.put("data",dataObject);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL,mainJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //TODO: handle success

                    Log.d("RESPONSE",response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    //TODO: handle failure
                    Log.d("ERROR",error.toString());
                }
            }){



                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", CONTENT_TYPE);
                    headers.put("Authorization", AUTHORIZATION_KEY);
                    return headers;
                }

            };
            Volley.newRequestQueue(context).add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendCancelNoti(String orderId,String token){
        JSONObject mainJsonObject = new JSONObject();

        JSONObject notificationObject = new JSONObject();
        JSONObject dataObject = new JSONObject();
        try {
            notificationObject.put("title","Cancel Order");
            notificationObject.put("body","Your Order Has been canceled");
            notificationObject.put("sound","default");

            dataObject.put(Constant.ORDER_ID,orderId);

            mainJsonObject.put("to",token);
            mainJsonObject.put("notification",notificationObject);
            mainJsonObject.put("data",dataObject);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL,mainJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //TODO: handle success

                    Log.d("RESPONSE",response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    //TODO: handle failure
                    Log.d("ERROR",error.toString());
                }
            }){



                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", CONTENT_TYPE);
                    headers.put("Authorization", AUTHORIZATION_KEY);
                    return headers;
                }

            };
            Volley.newRequestQueue(context).add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void customNotification(String orderId,String token,String title,String body){

        JSONObject mainJsonObject = new JSONObject();

        JSONObject notificationObject = new JSONObject();
        JSONObject dataObject = new JSONObject();
        try {
            notificationObject.put("title",title);
            notificationObject.put("body",body);
            notificationObject.put("sound","default");

            dataObject.put(Constant.ORDER_ID,orderId);

            mainJsonObject.put("to",token);
            mainJsonObject.put("notification",notificationObject);
            mainJsonObject.put("data",dataObject);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL,mainJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //TODO: handle success

                    Log.d("RESPONSE",response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    //TODO: handle failure
                    Log.d("ERROR",error.toString());
                }
            }){



                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", CONTENT_TYPE);
                    headers.put("Authorization", AUTHORIZATION_KEY);
                    return headers;
                }

            };
            Volley.newRequestQueue(context).add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void sendCustomNotification(final String orderId, String id, final String title, final String body){
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        myDatabaseReference.getUserRef().child(id).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String token = dataSnapshot.getValue(String.class);

                customNotification(orderId,token,title,body);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void sendOrderNotification(final String orderId, String id){

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        myDatabaseReference.getUserRef().child(id).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String token = dataSnapshot.getValue(String.class);

                sendNoti(orderId,token);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void cancelOrderNotification(final String orderId, String id){
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        myDatabaseReference.getUserRef().child(id).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String token = dataSnapshot.getValue(String.class);

                sendCancelNoti(orderId,token);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

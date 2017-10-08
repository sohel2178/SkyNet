package com.imatbd.skynet.Volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.imatbd.skynet.Model.Notification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Genius 03 on 7/22/2017.
 */

public class NotificationSender {
    private static final String URL="https://fcm.googleapis.com/fcm/send";
    private static final String AUTHORIZATION_KEY="key=AIzaSyAo-gscXkcEUvnRgyJEo7Z86pcidJ6O3Oc";
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("HHHH",jsonObject.toString());

        return jsonObject;
    }
}

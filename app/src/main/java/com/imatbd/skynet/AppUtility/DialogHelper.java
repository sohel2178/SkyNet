package com.imatbd.skynet.AppUtility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Genius 03 on 9/27/2017.
 */

public class DialogHelper {

    private Activity activity;

    private SweetAlertDialog pDialog;

    public DialogHelper(Activity activity) {
        this.activity = activity;
    }

    public void showProgressDialog(){
        pDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void dismisDialog(){
        pDialog.dismiss();
    }
}

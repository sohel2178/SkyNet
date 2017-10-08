package com.imatbd.skynet;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imatbd.skynet.Activities.BaseDetailActivity;
import com.imatbd.skynet.Activities.RegisterActivity;
import com.imatbd.skynet.AppUtility.UserLocalStore;
import com.imatbd.skynet.NavFragments.HomeFragment;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseDetailActivity implements View.OnClickListener{
    private static final int REQUIRED_PERMISSION=5000;

    private Handler handler = new Handler();

    private TextView tvTitle;
    private ImageView ivCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupWindowAnimations();

        doAftergettingPermission();

    }

    private void initAfterPermission(){
        Log.d("HHHH",UserLocalStore.getInstance(getApplicationContext()).isRegistered()+"");
        if(UserLocalStore.getInstance(getApplicationContext()).isRegistered()){
            setUpNavigationDrawer();

            tvTitle = (TextView) findViewById(R.id.title);
            ivCart = (ImageView) findViewById(R.id.cart);
            ivCart.setOnClickListener(this);

            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new HomeFragment())
                    .commit();

            Log.d("KKK","MainActivity OnCreate Called");

        }else{

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    //finishAfterTransition();
                    transitionTo(intent);
                }
            },1000);



            //startActivity(intent);

        }
    }

    public void setTitle(String title){
        if(tvTitle!= null){
            tvTitle.setText(title);
        }
    }

    public void setTitle(String title,String trName){
        if(tvTitle!=null){
            tvTitle.setTransitionName(trName);
            tvTitle.setText(title);
        }
    }

    public void showCart(){
        if(ivCart!=null){
            ivCart.setVisibility(View.VISIBLE);
        }
    }

    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

    @AfterPermissionGranted(REQUIRED_PERMISSION)
    private void doAftergettingPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...

            // initialize After Permission
            initAfterPermission();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permission_txt),
                    REQUIRED_PERMISSION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(ivCart)){

        }
    }
}

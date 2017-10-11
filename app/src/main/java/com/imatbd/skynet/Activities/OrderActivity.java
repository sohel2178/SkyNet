package com.imatbd.skynet.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.imatbd.skynet.R;

public class OrderActivity extends BaseDetailActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //showNotificationInToolbar();

        if(getUserType()==2){
            setTitle("Customers Orders");
        }


    }
}

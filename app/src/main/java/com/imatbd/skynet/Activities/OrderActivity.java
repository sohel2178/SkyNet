package com.imatbd.skynet.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.imatbd.skynet.Adapter.OrderViewPagerAdapter;
import com.imatbd.skynet.Firebase.MyDatabaseReference;
import com.imatbd.skynet.Model.Order;
import com.imatbd.skynet.R;
import com.imatbd.skynet.TabFragments.AcceptedOrderFragment;
import com.imatbd.skynet.TabFragments.CancelOrderFragment;
import com.imatbd.skynet.TabFragments.CompletedOrderFragment;
import com.imatbd.skynet.TabFragments.ForwardedOrderFragment;
import com.imatbd.skynet.TabFragments.PlaceOrderFragment;

public class OrderActivity extends BaseDetailActivity {

    OrderViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setViewPager();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //showNotificationInToolbar();

        if(getUserType()==2){
            setTitle("Customers Orders");
        }




    }



    @Override
    public void showCartContainer() {

    }

    @Override
    public void hideCartContainer() {

    }

}

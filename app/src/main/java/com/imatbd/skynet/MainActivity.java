package com.imatbd.skynet;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.imatbd.skynet.Activities.BaseDetailActivity;
import com.imatbd.skynet.Activities.RegisterActivity;
import com.imatbd.skynet.Activities.TestActivity;
import com.imatbd.skynet.Adapter.CartAdapter;
import com.imatbd.skynet.AppUtility.UserLocalStore;
import com.imatbd.skynet.Firebase.MyDatabaseReference;
import com.imatbd.skynet.Model.CartItem;
import com.imatbd.skynet.Model.Order;
import com.imatbd.skynet.NavFragments.CartFragment;
import com.imatbd.skynet.NavFragments.HomeFragment;
import com.imatbd.skynet.Utility.Constant;
import com.imatbd.skynet.Utility.CustomAnimator;
import com.imatbd.skynet.Volley.NotificationSender;

import java.io.Serializable;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseDetailActivity implements View.OnClickListener,
        CartAdapter.ItemRemoveListener{
    private static final int REQUIRED_PERMISSION=5000;

    private Handler handler = new Handler();

    private HomeFragment homeFragment;

    // View
    private Button btnOrderNow,btnNotiTest;
    private RecyclerView rvCart;


    private CartAdapter cartAdapter;

    // Two Container
    private RelativeLayout cartContainer;
    private FrameLayout mainContainer;

    // Database Reference
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent().getExtras()!=null){
            //Log.d("JJJJJJ",getIntent().getStringExtra(Constant.ORDER_ID));

            String orderId = getIntent().getStringExtra(Constant.ORDER_ID);

            if(orderId!=null){
                startOrderActivity(orderId);
            }

        }

        setupWindowAnimations();

        doAftergettingPermission();

    }

    private void initAfterPermission(){
        Log.d("HHHH",UserLocalStore.getInstance(getApplicationContext()).isRegistered()+"");
        if(UserLocalStore.getInstance(getApplicationContext()).isRegistered()){
            setUpNavigationDrawer();

            MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
            orderRef = myDatabaseReference.getOrderRef();

            cartAdapter = new CartAdapter(getApplicationContext());
            cartAdapter.setItemRemoveLIstener(this);

            cartContainer = findViewById(R.id.cart_container);
            mainContainer = findViewById(R.id.main_container);

            // Button in Cart Container
            btnOrderNow = findViewById(R.id.order_now);
            btnNotiTest = findViewById(R.id.notification_test);

            btnNotiTest.setOnClickListener(this);
            btnOrderNow.setOnClickListener(this);

            rvCart = findViewById(R.id.rv_cart);
            rvCart.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvCart.setAdapter(cartAdapter);


            homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,homeFragment)
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

    public  void showCartContainer(){

        CustomAnimator.slide(cartContainer, mainContainer,CustomAnimator.DIRECTION_LEFT, 400);
    }

    public void hideCartContainer(){
        CustomAnimator.reversePrevious();
    }

    @Override
    protected void onResume() {
        super.onResume();

        showNotificationInToolbar();

        if(getUserType()==3){
            Log.d("Meth","Method Call From Main Activity");
            showCart();
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

        switch (view.getId()){
            case R.id.order_now:
                if(isNetworkConnected()){
                    if(cartItemCount()>0){
                        placeOrder();
                    }else{
                        Toast.makeText(getApplicationContext(), "Cart is Empty", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please turn On your Internet Connection before place order", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.notification_test:
                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                intent.putExtra("data", (Serializable) cartAdapter.getCartItemList());
                startActivity(intent);
                break;
        }

    }

    private void placeOrder(){
        final Order order = new Order(0,cartAdapter.getCartItemList(),
                getUser().getId(),getUser().getAgentId(),getUser().getAdminId());

        orderRef.push().setValue(order, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                final String id = databaseReference.getKey();

                orderRef.child(id).child("order_id").setValue(id, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        // Notify Agent for this new Order
                        getUser().getAgentId();

                        NotificationSender notificationSender = new NotificationSender(getApplicationContext());
                        notificationSender.sendOrderNotification(id,getUser().getAgentId());


                        // Clear All Item from Cart Adapter
                        cartAdapter.clearCart();
                        setCartText(cartItemCount());
                        CustomAnimator.reversePrevious();


                    }
                });

            }
        });
    }


    public void addCartItem(CartItem cartItem){
        if(cartAdapter.isCartAdded(cartItem)){
            Toast.makeText(getApplicationContext(), "Item already added in the Cart", Toast.LENGTH_SHORT).show();
        }else{
            // Add item to the Adapter
            cartAdapter.addCartItem(cartItem);

            setCartText(cartAdapter.getItemCount());
        }
    }


    @Override
    public void removeItem() {

        setCartText(cartItemCount());

        // if All Item Remove Hide Cart View
        if(cartItemCount()==0){
            CustomAnimator.reversePrevious();
        }

    }


    private int cartItemCount(){
        return cartAdapter.getItemCount();
    }
}

package com.imatbd.skynet.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imatbd.skynet.Adapter.OrderViewPagerAdapter;
import com.imatbd.skynet.AppUtility.MyUtils;
import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.NavFragments.HomeFragment;
import com.imatbd.skynet.NavigationDrawer;
import com.imatbd.skynet.R;
import com.imatbd.skynet.TabFragments.AcceptedOrderFragment;
import com.imatbd.skynet.TabFragments.CancelOrderFragment;
import com.imatbd.skynet.TabFragments.ForwardedOrderFragment;
import com.imatbd.skynet.TabFragments.CompletedOrderFragment;
import com.imatbd.skynet.TabFragments.PlaceOrderFragment;
import com.imatbd.skynet.Utility.Constant;
import com.imatbd.skynet.Utility.TransitionHelper;


/**
 * Created by Genius 03 on 8/28/2017.
 */

public abstract class BaseDetailActivity extends AppCompatActivity {

    private Handler handler = new Handler();


    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private FragmentManager manager;

    private TextView tvTitle,tvCartText,tvNotiText;
    private RelativeLayout rlCart,rlNoti;
    private ImageView ivCartIcon,ivNoti;

    private boolean isCartClick=false;

    private NavigationDrawer drawerFragment;

    private UserData userData;


    private OrderViewPagerAdapter pagerAdapter;

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            activateNotifition();
            Toast.makeText(context, "Broad Cast Receive", Toast.LENGTH_SHORT).show();

        }
    };

    private void activateNotifition(){
        userData.increaseNotificationCount();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound);
                    mediaPlayer.start();
                    Thread.sleep(300);


                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setNotiText(userData.getNotificationCount());
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();




    }

    public void setNotiText(int value){
        if(value!=0){
            tvNotiText.setText(String.valueOf(value));
            ivNoti.setImageResource(R.drawable.noti_red);
        }else{
            tvNotiText.setText(String.valueOf(""));
            ivNoti.setImageResource(R.drawable.noti);
        }
    }




    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction(Constant.ACTION);
        registerReceiver(myReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(myReceiver);
    }

    public void setupToolbar() {
        userData = UserData.getInstance(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvTitle =  findViewById(R.id.title);
        tvCartText = findViewById(R.id.cart_text);
        tvNotiText = findViewById(R.id.noti_text);

        ivCartIcon = findViewById(R.id.cart_icon);
        ivNoti = findViewById(R.id.noti_icon);

        rlCart =  findViewById(R.id.cart);
        rlNoti =  findViewById(R.id.notification_container);

        // Handle Cart Item Click
        rlCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCartClick();
            }
        });

        // Handle Notification Click On ToolBar

        rlNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleNotificationClick();
            }
        });
    }

    // This Method is Responsible for Notification Click
    private void handleNotificationClick() {
        userData.resetNotificationCount();
        setNotiText(userData.getNotificationCount());
        Intent intent = new Intent(getApplicationContext(),OrderActivity.class);
        transitionTo(intent);
    }

    public void setViewPager(){
        setupToolbar();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        pagerAdapter = new OrderViewPagerAdapter(getSupportFragmentManager());

        if(getUserType()==3 || getUserType()==2){
            pagerAdapter.addFragment(new PlaceOrderFragment(), "PLACED");
        }
        pagerAdapter.addFragment(new ForwardedOrderFragment(), "FORWARDED");
        pagerAdapter.addFragment(new AcceptedOrderFragment(), "ACCEPTED");
        pagerAdapter.addFragment(new CompletedOrderFragment(), "COMPLETED");
        pagerAdapter.addFragment(new CancelOrderFragment(), "CANCELED");
        viewPager.setAdapter(pagerAdapter);
    }

    public OrderViewPagerAdapter getPagerAdapter(){
        return pagerAdapter;
    }

    public void showNotificationInToolbar(){
        if(rlNoti!=null){
            rlNoti.setVisibility(View.VISIBLE);
        }
    }


    public void setUpNavigationDrawer(){
        setupToolbar();

        manager = getSupportFragmentManager();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerFragment =
                (NavigationDrawer) manager.findFragmentById(R.id.fragment_navigation_drawer);


        drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, toolbar);
        //getSupportActionBar().setTitle(Constant.HOME);

        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressWarnings("unchecked")public void transitionTo(Intent i) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        startActivity(i, transitionActivityOptions.toBundle());
    }




    public void windowFadeAnimation(){
        Visibility enterTransition = buildEnterTransition();
        getWindow().setEnterTransition(enterTransition);

    }

    private Visibility buildEnterTransition() {
        Fade enterTransition = new Fade();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        enterTransition.excludeTarget(12,true); // You can replace any integer for |R.id.square_red|

        return enterTransition;

    }

    public void setCartText(int value){
        if(value!=0){
            tvCartText.setText(String.valueOf(value));
            ivCartIcon.setImageResource(R.drawable.cart_red);
        }else{
            tvCartText.setText(String.valueOf(""));
            ivCartIcon.setImageResource(R.drawable.cart);
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
        if(rlCart!=null){
            rlCart.setVisibility(View.VISIBLE);
        }
    }


    public User getUser(){
        return UserData.getInstance(getApplicationContext()).getUser();
    }

    public int getUserType(){
        return getUser().getUserType();
    }

    public void startOrderActivity(String orderId){
        Intent intent = new Intent(getApplicationContext(),OrderActivity.class);
        intent.putExtra(Constant.ORDER_ID,orderId);
        startActivity(intent);

    }

    public abstract void showCartContainer();
    public abstract void hideCartContainer();

    private void handleCartClick(){

        if(isCartClick){
            isCartClick=false;
            hideCartContainer();
        }else{
            isCartClick=true;
            showCartContainer();
        }



        if(getSupportFragmentManager().findFragmentById(R.id.main_container) instanceof HomeFragment){
            HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.main_container);

        }

    }

    public boolean isNetworkConnected(){
        return MyUtils.isNetworkConnected(getApplicationContext());
    }
}

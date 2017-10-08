package com.imatbd.skynet.Activities;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.NavigationDrawer;
import com.imatbd.skynet.R;
import com.imatbd.skynet.Utility.TransitionHelper;


/**
 * Created by Genius 03 on 8/28/2017.
 */

public class BaseDetailActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private FragmentManager manager;

    private NavigationDrawer drawerFragment;

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void setUpNavigationDrawer(){
        manager = getSupportFragmentManager();

        //Toolbar Code
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Drawer Layout Code
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


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

    public void setTitle(String title){
        TextView textView = (TextView) findViewById(R.id.title);

        if(textView != null){
            textView.setText(title);
        }
    }


    public User getUser(){
        return UserData.getInstance(getApplicationContext()).getUser();
    }

    public int getUserType(){
        return getUser().getUserType();
    }

}

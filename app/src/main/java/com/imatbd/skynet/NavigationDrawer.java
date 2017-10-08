package com.imatbd.skynet;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.imatbd.skynet.Adapter.NavAdapter;
import com.imatbd.skynet.AppUtility.MyUtils;
import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.AppUtility.UserLocalStore;
import com.imatbd.skynet.DialogFragments.UploadImageDialog;
import com.imatbd.skynet.Firebase.FdatabaseUtil;
import com.imatbd.skynet.Listener.NavItemClickListener;
import com.imatbd.skynet.Model.NavData;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.NavFragments.AgentListFragment;
import com.imatbd.skynet.NavFragments.BaseFragment;
import com.imatbd.skynet.NavFragments.CustomerListFragment;
import com.imatbd.skynet.NavFragments.HomeFragment;
import com.imatbd.skynet.Service.StorageCommunicationService;
import com.imatbd.skynet.Utility.Constant;
import com.imatbd.skynet.Utility.Method;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawer extends BaseFragment implements View.OnClickListener
    ,NavItemClickListener{

    public static final String PREF_NAME ="mypref";
    public static final String KEY_USER_LEARNED_DRAWERR="user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    private View containerView;

    private CircleImageView ivProfileImage,ivCamera;
    private TextView tvName;
    private RecyclerView rvNav;


    private User user;

    private NavAdapter navAdapter;




    public NavigationDrawer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWERR,"false"));

        // if saveInstanceState is not null its coming back from rotation
        if(savedInstanceState!=null){
            mFromSavedInstanceState=true;
        }



        if(UserLocalStore.getInstance(getContext()).isRegistered()){

            if(MyUtils.isNetworkConnected(getContext())){
                Log.d("SOHEL","Request User");
                FdatabaseUtil fdatabaseUtil = new FdatabaseUtil(getContext());

                Log.d("SOHEL",UserData.getInstance(getContext()).getUser().getId());
                fdatabaseUtil.getUserByID(UserData.getInstance(getContext()).getUser().getId());
                fdatabaseUtil.setListener(new FdatabaseUtil.UserListener() {
                    @Override
                    public void getUser(User user, int methodId) {
                        if(user!=null){
                            Log.d("SOHEL",user.getId());
                            NavigationDrawer.this.user = user;
                            UserData.getInstance(getContext()).saveUser(user);

                            Log.d("SOHEL","Request User");

                            //Log.d("SOHEL",user.getId());

                            bindDatainInfoSection();
                        }

                    }
                });
            }
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_navigation_drawer, container, false);


        //Initialize View

        initView(view);

        return view;
    }

    private void initView(View view) {

        ivProfileImage = view.findViewById(R.id.profile_image);
        ivCamera = view.findViewById(R.id.iv_camera);
        tvName = view.findViewById(R.id.tv_name);
        rvNav = view.findViewById(R.id.rv_nav);

        navAdapter = new NavAdapter(getContext());
        navAdapter.setListener(this);
        rvNav.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNav.setAdapter(navAdapter);



    }



    private void bindDatainInfoSection() {
        if(user==null){
            //user = UserData.getInstance(getContext()).getUser();
        }

        tvName.setText(user.getName());
        if(!user.getImageUrl().equals("")){

            Picasso.with(getContext())
                    .load(user.getImageUrl())
                    .into(ivProfileImage);

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ivCamera.setOnClickListener(this);





    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case Constant.PICTURE_REQUEST:
                if(resultCode== Activity.RESULT_OK){
                    Bundle bundle = data.getExtras();
                    String path = bundle.getString(Constant.URI);
                    Bitmap imageBitmap = MyUtils.resizeBitmap(path,150,150);
                    ivProfileImage.setImageBitmap(imageBitmap);

                    // start service for upload Image
                    if(imageBitmap!=null){
                        startStorageService(Method.UPLOAD_IMAGE,user.getId(),path);

                        Log.d("SOHEL","Upload Image Call");
                    }



                }
                break;
        }
    }



    public void setUp(int fragmentId, DrawerLayout layout, final Toolbar toolbar) {

        containerView = getActivity().findViewById(fragmentId);

        mDrawerLayout = layout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),mDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                //if user gonna not seen the drawer before thats mean the drawer is open for the first time

                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer=true;
                    // save it in sharedpreferences
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWERR,mUserLearnedDrawer+"");

                    getActivity().invalidateOptionsMenu();
                }

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                super.onDrawerSlide(drawerView, slideOffset);
            }
        };


        if(!mUserLearnedDrawer && !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }


    public static void saveToPreferences(Context context, String key, String prefValue){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,prefValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String key, String defaultValue){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return pref.getString(key,defaultValue);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_camera:
                UploadImageDialog uploadImageDialog = new UploadImageDialog();
                uploadImageDialog.setTargetFragment(this, Constant.PICTURE_REQUEST);
                uploadImageDialog.show(getFragmentManager(), Constant.DEFAULAT_FRAGMENT_TAG);
                break;

        }
    }


    private void addNextFragment(Fragment fragment ,boolean overlap){

        Slide slide = new Slide();
        slide.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

        ChangeBounds changeBoundsTransition = new ChangeBounds();
        changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

        fragment.setEnterTransition(slide);
        fragment.setAllowEnterTransitionOverlap(overlap);
        fragment.setAllowReturnTransitionOverlap(overlap);
        fragment.setSharedElementEnterTransition(changeBoundsTransition);

        getFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                //.addSharedElement(tvAboutUs, getString(R.string.about_us))
                .commit();

    }

    @Override
    public void onItemClick(NavData data) {

        switch (data.getName()){
            case Constant.HOME:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                transitFragment(new HomeFragment());
                break;

            case Constant.AGENT_LIST:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                /*getFragmentManager().beginTransaction().replace(R.id.main_container,new AgentListFragment())
                        .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null).commit();*/
                transitFragment(new AgentListFragment());
                break;

            case Constant.CUSTOMER_LIST:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                transitFragment(new CustomerListFragment());
                break;
        }

    }


    private class MyResultReceiver extends ResultReceiver {

        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            if(resultCode==Constant.SERVICE_REQ_CODE && resultData!=null){

                int method = resultData.getInt(Constant.METHOD_REQ);

                switch (method){

                    case Method.UPLOAD_IMAGE:
                        String url = resultData.getString(Constant.URL);

                        Toast.makeText(getContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        break;

                }



            }



            Log.d("SOHEL","RESULT RECEIVE");
        }
    }

    private Intent getStorageIntent(int method){
        MyResultReceiver resultReceiver = new MyResultReceiver(null);
        Intent intent = new Intent(getContext(), StorageCommunicationService.class);
        intent.putExtra(Constant.RECEIVER,resultReceiver);
        intent.putExtra(Constant.METHOD_REQ,method);
        return intent;
    }

    private Intent getStorageIntent(int method,String id, String path){
        Intent intent = getStorageIntent(method);
        intent.putExtra(Constant.PATH,path);
        intent.putExtra(Constant.ID,id);
        return intent;
    }

    public void startStorageService(int method){
        Intent intent = getStorageIntent(method);
        getContext().startService(intent);
    }

    public void startStorageService(int method,String id, String path){
        Intent intent = getStorageIntent(method,id,path);
        getContext().startService(intent);
    }





}

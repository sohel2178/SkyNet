package com.imatbd.skynet.NavFragments;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.imatbd.skynet.AppUtility.MyUtils;
import com.imatbd.skynet.Firebase.MyDatabaseReference;
import com.imatbd.skynet.MainActivity;
import com.imatbd.skynet.Model.Product;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.R;
import com.imatbd.skynet.Utility.Constant;

/**
 * Created by Genius 03 on 10/4/2017.
 */

public class BaseFragment extends Fragment {

    public void setTitle(String title){
        if(getActivity() instanceof MainActivity){
            MainActivity activity = (MainActivity) getActivity();
            activity.setTitle(title);
        }
    }

    public void setTitle(String title,String trName){
        if(getActivity() instanceof MainActivity){
            MainActivity activity = (MainActivity) getActivity();
            activity.setTitle(title,trName);
        }
    }

    public void showCartIcon(){
        if(getActivity() instanceof MainActivity){
            MainActivity activity = (MainActivity) getActivity();
            activity.showCart();
        }
    }

    public void setCartText(int value){
        if(getActivity() instanceof MainActivity){
            MainActivity activity = (MainActivity) getActivity();
            activity.setCartText(value);
        }
    }

    public void transitFragment(Fragment newFragment){
        // Get the Existing Fragment
        Fragment oldFragment = getFragmentManager().findFragmentById(R.id.main_container);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        Fade exitFade = new Fade();
        exitFade.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        oldFragment.setExitTransition(exitFade);

        Fade enterFade = new Fade();
        enterFade.setStartDelay(getResources().getInteger(R.integer.anim_duration_medium));
        enterFade.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        newFragment.setEnterTransition(enterFade);

        //oldFragment.setReenterTransition(enterFade);

        fragmentTransaction.replace(R.id.main_container, newFragment,newFragment.getClass().getName()).addToBackStack(newFragment.getClass().getName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void transitFragmentWithSharedElement(Fragment newFragment,View view,String transitionName){
        // Get the Existing Fragment
        Fragment oldFragment = getFragmentManager().findFragmentById(R.id.main_container);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        Fade exitFade = new Fade();
        exitFade.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        oldFragment.setExitTransition(exitFade);

        // 2. Shared Elements Transition
        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        enterTransitionSet.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        enterTransitionSet.setStartDelay(100);
        newFragment.setSharedElementEnterTransition(enterTransitionSet);

        Fade enterFade = new Fade();
        enterFade.setStartDelay(getResources().getInteger(R.integer.anim_duration_medium)+100);
        enterFade.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        newFragment.setEnterTransition(enterFade);

        //oldFragment.setReenterTransition(enterFade);

        fragmentTransaction.addSharedElement(view,transitionName);

        fragmentTransaction.replace(R.id.main_container, newFragment).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void transitFragmentWithSharedEle(Fragment newFragment, View view,String trName){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            Transition returnTransition = TransitionInflater.from(getContext())
                    .inflateTransition(R.transition.default_transition);

            Transition exitTransition = TransitionInflater.from(getContext())
                    .inflateTransition(android.R.transition.no_transition);

            setSharedElementReturnTransition(returnTransition);
            setExitTransition(exitTransition);

            newFragment.setSharedElementEnterTransition(returnTransition);
            newFragment.setEnterTransition(exitTransition);

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, newFragment, "sohel");
            fragmentTransaction.addToBackStack("sohel");
            fragmentTransaction.addSharedElement(view, trName);
            fragmentTransaction.commit();


        }
    }

    private void addNextFragment(boolean overlap, Fragment fragment,
                                 View view,String transitionName){

        Slide slide = new Slide();
        slide.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

        Fade fade = new Fade();
        fade.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

        ChangeBounds changeBoundsTransition = new ChangeBounds();
        changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

        fragment.setEnterTransition(fade);
        fragment.setAllowEnterTransitionOverlap(overlap);
        fragment.setAllowReturnTransitionOverlap(overlap);
        fragment.setSharedElementEnterTransition(changeBoundsTransition);

        getFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .addSharedElement(view, transitionName)
                .commit();
    }

    public void shareCode(User user){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, user.getId());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    public User getUser(){
        MainActivity activity=null;
        if(getActivity() instanceof MainActivity){
            activity = (MainActivity) getActivity();
        }

        return activity.getUser();
    }

    public int getUserType(){
        return getUser().getUserType();
    }

    public String getUserId(){
        return getUser().getId();
    }

    public boolean isNetworkConnected(){
        return MyUtils.isNetworkConnected(getContext());
    }

    public void increaseViewCount(final Product product) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(isNetworkConnected()){
                    MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
                    DatabaseReference prodRef = myDatabaseReference.getProductRef();
                    prodRef.child(product.getId()).child("viewCount").setValue(product.getViewCount()+1);
                }
            }
        });
        thread.start();



    }

}

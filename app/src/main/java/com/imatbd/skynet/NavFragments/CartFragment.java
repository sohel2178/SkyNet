package com.imatbd.skynet.NavFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imatbd.skynet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends BaseFragment {


    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        
        initView(view);
        return view;
    }

    private void initView(View view) {

    }

}

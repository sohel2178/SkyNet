package com.imatbd.skynet.TabFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imatbd.skynet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AcceptedOrderFragment extends Fragment {


    public AcceptedOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accepted_order, container, false);
    }

}

package com.imatbd.skynet.Listener;

import android.view.View;

import com.imatbd.skynet.Model.Product;

/**
 * Created by Genius 03 on 10/2/2017.
 */

public interface ProductClickListener {
    // Update Image Type 1
    // Update Price Type 2
    // Add Quantity Type 3
    // On ItemClick Type 4
    public void onItemClick(Product product,int position, int updateType, View view);
}

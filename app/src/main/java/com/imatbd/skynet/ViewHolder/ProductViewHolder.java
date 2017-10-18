package com.imatbd.skynet.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

/**
 * Created by Genius 03 on 10/18/2017.
 */

public class ProductViewHolder extends ChildViewHolder {
    public TextView tvName,tvDesc,tvPrice,tvAvailableQuantity;
    public ImageView ivProductImage,ivCartPlus;
    public Button btnUpdateImage,btnUpdatePrice,btnAddQuantity;
    public ProductViewHolder(View itemView) {
        super(itemView);
    }
}

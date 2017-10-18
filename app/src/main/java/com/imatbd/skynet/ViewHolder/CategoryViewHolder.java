package com.imatbd.skynet.ViewHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.imatbd.skynet.R;

/**
 * Created by Genius 03 on 10/18/2017.
 */

public class CategoryViewHolder extends ParentViewHolder {
    public TextView tvCat;
    public ImageButton ivExpand;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        tvCat = itemView.findViewById(R.id.tv_cat);
        ivExpand = itemView.findViewById(R.id.iv_expand);
    }

    @Override
    public boolean isExpanded() {
        return super.isExpanded();
    }
}

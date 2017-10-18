package com.imatbd.skynet.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.Listener.ProductClickListener;
import com.imatbd.skynet.Model.Product;
import com.imatbd.skynet.Model.ProductCategory;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Genius 03 on 10/18/2017.
 */

public class TestAdapter extends ExpandableRecyclerAdapter<TestAdapter.CategoryViewHolder,TestAdapter.ProductViewHolder> {
    private Context context;
    private ProductClickListener listener;
    private LayoutInflater inflater;
    private List<ParentObject> parentObjectList;

    public TestAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        this.parentObjectList = parentItemList;
        this.context=context;
        this.inflater = LayoutInflater.from(context);
    }


    public void addProduct(Product product){

    }



    public void setListener(ProductClickListener listener){
        this.listener = listener;
    }

    @Override
    public CategoryViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.single_category,viewGroup,false);

        return new CategoryViewHolder(view);
    }

    @Override
    public ProductViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.single_product,viewGroup,false);

        ProductViewHolder holder = new ProductViewHolder(view);

        view.setTag(holder);

        return holder;
    }

    @Override
    public void onBindParentViewHolder(CategoryViewHolder holder, int i, Object o) {
        ProductCategory productCategory = (ProductCategory) o;
        holder.tvCat.setText(productCategory.getCategory());

    }

    @Override
    public void onBindChildViewHolder(ProductViewHolder productViewHolder, int i, Object o) {

    }


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



    public class ProductViewHolder extends ChildViewHolder implements View.OnClickListener {
        public TextView tvName,tvDesc,tvPrice,tvAvailableQuantity;
        public ImageView ivProductImage,ivCartPlus;
        public Button btnUpdateImage,btnUpdatePrice,btnAddQuantity;

        public ProductViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.prodName);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvAvailableQuantity = itemView.findViewById(R.id.tv_available_quantity);
            ivProductImage = itemView.findViewById(R.id.iv_image);
            ivCartPlus = itemView.findViewById(R.id.cart_plus);

            btnUpdateImage = itemView.findViewById(R.id.btn_update_image);
            btnUpdatePrice = itemView.findViewById(R.id.btn_update_price);
            btnAddQuantity = itemView.findViewById(R.id.btn_add_quantity);

            User user = UserData.getInstance(context).getUser();

            Log.d("HHHH",user.getUserType()+"");

            if(user.getUserType()==1){
                btnUpdateImage.setOnClickListener(this);
                btnUpdatePrice.setOnClickListener(this);
                btnAddQuantity.setOnClickListener(this);
            }else if(user.getUserType()==2){
                btnUpdateImage.setVisibility(View.GONE);
                btnUpdatePrice.setVisibility(View.GONE);
                btnAddQuantity.setVisibility(View.GONE);
            }else if(user.getUserType()==3){
                btnUpdateImage.setVisibility(View.GONE);
                btnUpdatePrice.setVisibility(View.GONE);
                btnAddQuantity.setVisibility(View.GONE);

                ivCartPlus.setVisibility(View.VISIBLE);
                ivCartPlus.setOnClickListener(this);
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.equals(btnUpdateImage)){
                if(listener!=null){
                    listener.onItemClick(getAdapterPosition(),1,itemView);
                }

            }else if(view.equals(btnUpdatePrice)){
                if(listener!=null){
                    listener.onItemClick(getAdapterPosition(),2,itemView);
                }

            }else if(view.equals(btnAddQuantity)){
                if(listener!=null){
                    listener.onItemClick(getAdapterPosition(),3,itemView);
                }

            }else if(view.equals(ivCartPlus)){
                if(listener!=null){
                    listener.onItemClick(getAdapterPosition(),5,itemView);
                }

            }else if(view.equals(itemView)){
                if(listener!=null){
                    listener.onItemClick(getAdapterPosition(),4,itemView);
                }

            }
        }
    }
}

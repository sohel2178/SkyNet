package com.imatbd.skynet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.Firebase.MyDatabaseReference;
import com.imatbd.skynet.Listener.ProductClickListener;
import com.imatbd.skynet.Model.Product;
import com.imatbd.skynet.Model.Purchase;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Genius 03 on 10/2/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private Context context;
    private List<Product> productList;
    private LayoutInflater inflater;
    private ProductClickListener listener;


    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.inflater = LayoutInflater.from(context);
    }

    public void setListener(ProductClickListener listener){
        this.listener = listener;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_product,parent,false);

        ProductHolder holder = new ProductHolder(view);

        view.setTag(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, int position) {



        holder.ivProductImage.setTransitionName("transitionImage" + position);

        Product product = productList.get(position);

        holder.tvName.setText(product.getName());
        holder.tvDesc.setText(product.getDesciption());
        holder.tvPrice.setText(String.valueOf(product.getPrice()));
        holder.tvAvailableQuantity.setText(String.valueOf(product.getAvailableQuantity()));


        //Load image from server
        if(!TextUtils.isEmpty(product.getImageUrl())){
            Picasso.with(context)
                    .load(product.getImageUrl())
                    .into(holder.ivProductImage);
        }else{
            holder.ivProductImage.setImageResource(R.drawable.product_image);
        }

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();

        myDatabaseReference.getPurchaseRef().orderByChild("product_id").equalTo(product.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int quantity=0;
                        for (DataSnapshot x: dataSnapshot.getChildren()){
                            Purchase purchase = x.getValue(Purchase.class);
                            quantity = quantity+purchase.getQuantity();
                        }

                        int prevQ = Integer.parseInt(holder.tvAvailableQuantity.getText().toString());

                        holder.tvAvailableQuantity.setText(String.valueOf(prevQ+quantity));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    public void addProduct(Product product){
        productList.add(product);

        int position = productList.indexOf(product);

        notifyItemInserted(position);
    }

    public void updateProduct(Product product){

        int position = getPosition(product);

        if(position!=-1){
            productList.set(position,product);
            notifyItemChanged(position);
        }

    }

    private int getPosition(Product product){
        int position =-1;
        for (int i=0;i<productList.size();i++){
            if(productList.get(i).getCreatedTime()==product.getCreatedTime()){
                position=i;
                break;
            }
        }

        return position;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvName,tvDesc,tvPrice,tvAvailableQuantity;
        public ImageView ivProductImage,ivCartPlus;
        Button btnUpdateImage,btnUpdatePrice,btnAddQuantity;

        public ProductHolder(View itemView) {
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

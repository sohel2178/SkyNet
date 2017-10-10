package com.imatbd.skynet.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imatbd.skynet.Model.CartItem;
import com.imatbd.skynet.Model.Product;
import com.imatbd.skynet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Genius 03 on 10/10/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    private Context context;
    private List<CartItem> cartItemList;
    private LayoutInflater inflater;

    private ItemRemoveListener listener;

    public CartAdapter(Context context) {
        this.context = context;
        this.cartItemList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_cart_item,parent,false);

        CartHolder holder = new CartHolder(view);
        return holder;
    }

    public void setItemRemoveLIstener(ItemRemoveListener listener){
        this.listener =listener;
    }

    @Override
    public void onBindViewHolder(CartHolder holder, int position) {

        CartItem cartItem = cartItemList.get(position);
        Product product = cartItem.getProduct();

        holder.tvProdName.setText(product.getName());
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

        if(!product.getImageUrl().equals("")){
            Picasso.with(context)
                    .load(product.getImageUrl())
                    .into(holder.ivImage);
        }else{
            holder.ivImage.setImageResource(R.drawable.product_image);
        }

    }

    public void addCartItem(CartItem cartItem){
        cartItemList.add(cartItem);

        int position = cartItemList.indexOf(cartItem);

        notifyItemInserted(position);
    }

    private void removeCartItem(int position){
        cartItemList.remove(position);
        notifyItemRemoved(position);

        if(listener!=null){
            listener.removeItem();
        }
    }

    public void clearCart(){
        cartItemList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<CartItem> getCartItemList(){
        return cartItemList;
    }


    private void increaseQuantity(int position){
        CartItem cartItem = cartItemList.get(position);
        int quantity = cartItem.getQuantity()+1;
        cartItem.setQuantity(quantity);

        notifyItemChanged(position);
    }

    private void decreaseQuantity(int position){
        CartItem cartItem = cartItemList.get(position);

        if(cartItem.getQuantity()!=1){
            int quantity = cartItem.getQuantity()-1;
            cartItem.setQuantity(quantity);

            notifyItemChanged(position);
        }

    }

    public boolean isCartAdded(CartItem cartItem){
        boolean retBool = false;
        String prodId = cartItem.getProduct().getId();

        for (CartItem x:cartItemList){
            if(x.getProduct().getId().equals(prodId)){
                retBool=true;
                break;
            }
        }
        return retBool;
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }


    public class CartHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvProdName,tvQuantity;

        ImageView ivImage,ivPlus,ivMinus,ivCancel;

        public CartHolder(View itemView) {
            super(itemView);

            tvProdName = itemView.findViewById(R.id.prodName);
            tvQuantity = itemView.findViewById(R.id.quantity);
            ivImage = itemView.findViewById(R.id.image);
            ivPlus = itemView.findViewById(R.id.plus);
            ivMinus = itemView.findViewById(R.id.minus);
            ivCancel = itemView.findViewById(R.id.cancel);

            ivCancel.setOnClickListener(this);
            ivPlus.setOnClickListener(this);
            ivMinus.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view.equals(ivCancel)){
                removeCartItem(getAdapterPosition());
            }else if(view.equals(ivPlus)){
                increaseQuantity(getAdapterPosition());
            }else if(view.equals(ivMinus)){
                decreaseQuantity(getAdapterPosition());
            }

        }
    }


    public interface ItemRemoveListener{
        public void removeItem();
    }



}

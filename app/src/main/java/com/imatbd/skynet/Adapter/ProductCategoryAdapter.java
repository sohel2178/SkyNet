package com.imatbd.skynet.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.Firebase.MyDatabaseReference;
import com.imatbd.skynet.Listener.ProductClickListener;
import com.imatbd.skynet.Model.Product;
import com.imatbd.skynet.Model.ProductCategory;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.R;
import com.imatbd.skynet.Utility.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Genius 03 on 10/19/2017.
 */

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.CategoryViewHolder> implements ChildEventListener {
    private Context context;
    private List<ProductCategory> productCategoryList;
    private LayoutInflater inflater;
    private int expandedPosition = -1;

    private ProductClickListener productClickListener;

    private Query productQuery;


    public ProductCategoryAdapter(Context context,ProductClickListener productClickListener) {
        this.context = context;
        productCategoryList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);

        this.productClickListener = productClickListener;
        setUpFirebaseDatabase();
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_category,parent,false);

        CategoryViewHolder holder = new CategoryViewHolder(view);
        view.setTag(holder);

        return holder;
    }

    public void setExpandedPosition(int expandedPosition){
        this.expandedPosition=expandedPosition;
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, int position) {
        ProductCategory productCategory = productCategoryList.get(position);

        holder.tvCat.setText(productCategory.getCategory());

        Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_from_top);
        Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_from_bottom);

        slideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                holder.rvProduct.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        holder.rvProduct.setLayoutManager(new LinearLayoutManager(context));
        productCategory.getProductAdapter().setListener(productClickListener);
        holder.rvProduct.setAdapter(productCategory.getProductAdapter());

        if(productCategory.isExpand()){
            holder.rvProduct.setVisibility(View.VISIBLE);
            holder.rvProduct.startAnimation(slideDown);
        }else{
            holder.rvProduct.startAnimation(slideUp);
        }






    }

    @Override
    public int getItemCount() {
        return productCategoryList.size();
    }


    public void destroy(){
        productQuery.removeEventListener(this);
    }


    private void setUpFirebaseDatabase(){
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        DatabaseReference productRef = myDatabaseReference.getProductRef();
        User user = UserData.getInstance(context).getUser();
        String adminId;
        if(user.getUserType()==1){
            adminId = user.getId();
        }else{
            adminId= user.getAdminId();
        }

        productQuery =productRef.orderByChild(Constant.ADMIN_ID).equalTo(adminId);
        productQuery.addChildEventListener(this);

    }



    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvCat;
        public ImageButton ivExpand;
        public RecyclerView rvProduct;


        public CategoryViewHolder(View itemView) {
            super(itemView);

            tvCat = itemView.findViewById(R.id.tv_cat);
            ivExpand = itemView.findViewById(R.id.iv_expand);
            rvProduct = itemView.findViewById(R.id.rv_product);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            ProductCategory productCategory = productCategoryList.get(getAdapterPosition());

            if(productCategory.isExpand()){
                productCategory.setExpand(false);
            }else{
                productCategory.setExpand(true);
            }

            notifyItemChanged(getAdapterPosition());

        }
    }

    private ProductCategory getExistingCategory(String category){
        ProductCategory productCategory = null;
        for(ProductCategory x: productCategoryList){

            if(x.getCategory().equals(category)){
                productCategory = x;
                break;
            }
        }

        return productCategory;
    }

    public void addProduct(Product product){

        if(getExistingCategory(product.getCategory())!=null){
            getExistingCategory(product.getCategory()).addProduct(product);
        }else{
            productCategoryList.add(new ProductCategory(product,context));
        }

        notifyDataSetChanged();



    }




    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Product product = dataSnapshot.getValue(Product.class);
        addProduct(product);

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Product product = dataSnapshot.getValue(Product.class);

        Log.d("SOHEL","onChildChanged Called");

        ProductCategory productCategory = getExistingCategory(product.getCategory());

        int index = productCategoryList.indexOf(productCategory);

        if(productCategory!=null){
            Log.d("SOHEL","productCategory Not Null");

            List<Product> productList = productCategory.getProductList();

            int position = getPosition(productList,product);

            if(position!=-1){
                Log.d("SOHEL","Position not  -1 ");
                Log.d("SOHEL","Position "+position);

                productCategoryList.get(index).getProductAdapter().updateProduct(product);

                //productList.set(position,product);
               // notifyItemChanged(index);
            }

        }


    }

    public int getExpandedPosition(Product product){
        int expandedPos = -1;

        ProductCategory productCategory = getExistingCategory(product.getCategory());

        if(productCategory!=null){
            expandedPos = productCategoryList.indexOf(productCategory);
        }
        return expandedPos;
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


    private int getPosition(List<Product> productList,Product product){
        int position =-1;
        for (int i=0;i<productList.size();i++){
            if(productList.get(i).getCreatedTime()==product.getCreatedTime()){
                position=i;
                break;
            }
        }

        return position;
    }
}

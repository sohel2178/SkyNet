package com.imatbd.skynet.NavFragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.imatbd.skynet.Activities.AddProductActivity;
import com.imatbd.skynet.Activities.TestActivity;
import com.imatbd.skynet.Adapter.CartAdapter;
import com.imatbd.skynet.Adapter.ProductAdapter;
import com.imatbd.skynet.Adapter.ProductCategoryAdapter;
import com.imatbd.skynet.DialogFragments.UploadImageDialog;
import com.imatbd.skynet.Firebase.MyDatabaseReference;
import com.imatbd.skynet.Listener.ProductClickListener;
import com.imatbd.skynet.MainActivity;
import com.imatbd.skynet.Model.CartItem;
import com.imatbd.skynet.Model.Order;
import com.imatbd.skynet.Model.Product;
import com.imatbd.skynet.R;
import com.imatbd.skynet.Service.StorageCommunicationService;
import com.imatbd.skynet.Utility.Constant;
import com.imatbd.skynet.Utility.CustomAnimator;
import com.imatbd.skynet.Utility.Method;
import com.imatbd.skynet.Volley.NotificationSender;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragmenet extends BaseFragment implements CartAdapter.ItemRemoveListener,
        ProductClickListener{

    private RecyclerView rvProduct;
    private FloatingActionButton fabPlus;

    private ProductCategoryAdapter productCategoryAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private int expandPosition=-1;



    // Two Container
    private RelativeLayout prodContainer,cartContainer;


    public ProductsFragmenet() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("MMMMM","onCreate Called");


        productCategoryAdapter = new ProductCategoryAdapter(getContext(),this);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products_fragmenet, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        // init Container Here
        prodContainer = view.findViewById(R.id.prod_container);
        cartContainer = view.findViewById(R.id.cart_container);

        fabPlus = view.findViewById(R.id.fabPlus);
        rvProduct = view.findViewById(R.id.rv_product);
        layoutManager = new LinearLayoutManager(getContext());
        rvProduct.setLayoutManager(layoutManager);
        rvProduct.setAdapter(productCategoryAdapter);




        if(getUserType()==1){
            fabPlus.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(Constant.PRODUCTS);

        Log.d("KKKK",expandPosition+"");

        if(expandPosition!=-1){
            productCategoryAdapter.setExpandedPosition(expandPosition);
        }



    }


    @Override
    public void onDestroy() {
        productCategoryAdapter.destroy();
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Onclici Fab Button
        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();

                if(activity instanceof MainActivity){
                    MainActivity mainActivity = (MainActivity) activity;
                    mainActivity.transitionTo(new Intent(getContext(), AddProductActivity.class));
                }

            }
        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case Constant.PICTURE_REQUEST:
                if(resultCode== Activity.RESULT_OK){
                    Bundle bundle = data.getExtras();
                    String path = bundle.getString(Constant.URI);

                    String id = bundle.getString(Constant.ID);

                    if(path!=null){
                        startStorageService(Method.UPLOAD_PRODUCT_IMAGE,id,path);
                    }



                }
                break;
        }
    }


    private Intent getStorageIntent(int method){
        MyResultReceiver resultReceiver = new MyResultReceiver(null);
        Intent intent = new Intent(getContext(), StorageCommunicationService.class);
        intent.putExtra(Constant.RECEIVER,resultReceiver);
        intent.putExtra(Constant.METHOD_REQ,method);
        return intent;
    }

    private Intent getStorageIntent(int method,String id, String path){
        Intent intent = getStorageIntent(method);
        intent.putExtra(Constant.PATH,path);
        intent.putExtra(Constant.ID,id);
        return intent;
    }

    public void startStorageService(int method){
        Intent intent = getStorageIntent(method);
        getContext().startService(intent);
    }

    public void startStorageService(int method,String id, String path){
        Intent intent = getStorageIntent(method,id,path);
        getContext().startService(intent);
    }


    private class MyResultReceiver extends ResultReceiver {

        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            if(resultCode==Constant.SERVICE_REQ_CODE && resultData!=null){

                int method = resultData.getInt(Constant.METHOD_REQ);

                switch (method){

                    case Method.UPLOAD_PRODUCT_IMAGE:
                        String url = resultData.getString(Constant.URL);

                        Toast.makeText(getContext(), "Product Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        break;

                }



            }


        }
    }


    //This Method is remove item from Cart

    @Override
    public void removeItem() {


    }

    @Override
    public void onItemClick(Product product, int position, int updateType, View view) {

        switch (updateType){
            case 1:

                // Open Dialog Fragment Take Pick and Save its Result in OnActivity Result

                Bundle bundle = new Bundle();
                bundle.putString(Constant.ID,product.getId());
                UploadImageDialog uploadImageDialog = new UploadImageDialog();
                uploadImageDialog.setArguments(bundle);
                uploadImageDialog.setTargetFragment(this, Constant.PICTURE_REQUEST);
                uploadImageDialog.show(getFragmentManager(), Constant.DEFAULAT_FRAGMENT_TAG);
                break;

            case 2:
                break;

            case 3:
                AddProductFragment addProductFragment = new AddProductFragment();
                Bundle prodBundle = new Bundle();
                prodBundle.putSerializable(Constant.PRODUCT,product);
                expandPosition = productCategoryAdapter.getExpandedPosition(product);
                addProductFragment.setArguments(prodBundle);
                transitFragment(addProductFragment);
                break;

            case 4:
                // Build Transition Name
                String transitionImageName = "transitionImage" + position;

                expandPosition = productCategoryAdapter.getExpandedPosition(product);

                ProductAdapter.ProductHolder holder = (ProductAdapter.ProductHolder) view.getTag();
                UpdateProductFragment updateProductFragment = new UpdateProductFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable(Constant.PRODUCT,product);
                bundle1.putString(Constant.TRANSITION_NAME,transitionImageName);
                updateProductFragment.setArguments(bundle1);
                transitFragmentWithSharedEle(updateProductFragment,holder.ivProductImage,transitionImageName);
                //transitFragmentWithSharedElement(updateProductFragment,holder.ivProductImage,transitionImageName);
                //addNextFragment(true,updateProductFragment,holder.ivProductImage,transitionImageName);
                break;

            case 5: // Add to Cart Click

                CartItem cartItem = new CartItem(product);

                if(getActivity() instanceof MainActivity){
                    MainActivity activity = (MainActivity) getActivity();

                    activity.addCartItem(cartItem);
                }





                break;
        }
    }
}

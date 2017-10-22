package com.imatbd.skynet.NavFragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.imatbd.skynet.Activities.AddProductActivity;
import com.imatbd.skynet.Activities.TestActivity;
import com.imatbd.skynet.Adapter.CartAdapter;
import com.imatbd.skynet.Adapter.ProductAdapter;
import com.imatbd.skynet.AppUtility.MyUtils;
import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.DialogFragments.UploadImageDialog;
import com.imatbd.skynet.Firebase.MyDatabaseReference;
import com.imatbd.skynet.Listener.ProductClickListener;
import com.imatbd.skynet.MainActivity;
import com.imatbd.skynet.Model.CartItem;
import com.imatbd.skynet.Model.Notification;
import com.imatbd.skynet.Model.Order;
import com.imatbd.skynet.Model.Product;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.NavigationDrawer;
import com.imatbd.skynet.R;
import com.imatbd.skynet.Service.StorageCommunicationService;
import com.imatbd.skynet.Utility.Constant;
import com.imatbd.skynet.Utility.CustomAnimator;
import com.imatbd.skynet.Utility.Method;
import com.imatbd.skynet.Volley.NotificationSender;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements ProductClickListener,
        ChildEventListener,CartAdapter.ItemRemoveListener{

    private RecyclerView rvProduct,rvCart;
    private FloatingActionButton fabPlus;
    private Button btnOrderNow,btnNotiTest;
    private List<Product> productList;

    private ProductAdapter adapter;

    private CartAdapter cartAdapter;

    private Query productQuery;

    private DatabaseReference orderRef;

    private Product currentClickedProduct;

    private RelativeLayout prodContainer;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpFirebaseDatabase();

        productList = new ArrayList<>();
        adapter = new ProductAdapter(getContext(),productList);
        adapter.setListener(this);

        cartAdapter = new CartAdapter(getContext());
        cartAdapter.setItemRemoveLIstener(this);


    }

    private void setUpFirebaseDatabase(){
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        DatabaseReference productRef = myDatabaseReference.getProductRef();
        User user = UserData.getInstance(getContext()).getUser();
        String adminId;
        if(user.getUserType()==1){
            adminId = user.getId();
        }else{
            adminId= user.getAdminId();
        }

        orderRef = myDatabaseReference.getOrderRef();

        productQuery =productRef.orderByChild(Constant.ADMIN_ID).equalTo(adminId);
        productQuery.addChildEventListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(Constant.HOME);

        if(getUserType()==3){
            showCartIcon();
        }


    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onDestroy() {
        productQuery.removeEventListener(this);
        super.onDestroy();
    }

    private void initView(View view) {
        // init Container Here
        prodContainer = view.findViewById(R.id.prod_container);

        fabPlus = view.findViewById(R.id.fabPlus);
        btnOrderNow = view.findViewById(R.id.order_now);
        btnNotiTest = view.findViewById(R.id.notification_test);
        rvProduct = view.findViewById(R.id.rv_product);
        rvProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProduct.setAdapter(adapter);

        rvCart = view.findViewById(R.id.rv_cart);
        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCart.setAdapter(cartAdapter);


        if(getUserType()==1){
            fabPlus.setVisibility(View.VISIBLE);
        }
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

        btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Push Order to database

                if(isNetworkConnected()){
                    if(cartItemCount()>0){
                        placeOrder();
                    }else{
                        Toast.makeText(getContext(), "Cart is Empty", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Please turn On your Internet Connection before place order", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNotiTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TestActivity.class);
                intent.putExtra("data", (Serializable) cartAdapter.getCartItemList());
                startActivity(intent);
            }
        });
    }


    private void placeOrder(){
        final Order order = new Order(0,cartAdapter.getCartItemList(),
                getUserId(),getUser().getAgentId(),getUser().getAdminId());

        orderRef.push().setValue(order, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                final String id = databaseReference.getKey();

                orderRef.child(id).child("order_id").setValue(id, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        // Notify Agent for this new Order
                        getUser().getAgentId();

                        NotificationSender notificationSender = new NotificationSender(getContext());
                        notificationSender.sendOrderNotification(id,getUser().getAgentId());


                        // Clear All Item from Cart Adapter
                        cartAdapter.clearCart();
                        setCartText(cartItemCount());
                        CustomAnimator.reversePrevious();


                    }
                });

            }
        });
    }

    @Override
    public void onItemClick(Product product,int position, int updateType,View view) {


        /*switch (updateType){
            case 1:

                // Open Dialog Fragment Take Pick and Save its Result in OnActivity Result

                Bundle bundle = new Bundle();
                bundle.putString(Constant.ID,currentClickedProduct.getId());
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
                prodBundle.putSerializable(Constant.PRODUCT,productList.get(position));

                addProductFragment.setArguments(prodBundle);
                transitFragment(addProductFragment);
                break;

            case 4:
                // Build Transition Name
                String transitionImageName = "transitionImage" + position;

                ProductAdapter.ProductHolder holder = (ProductAdapter.ProductHolder) view.getTag();
                UpdateProductFragment updateProductFragment = new UpdateProductFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable(Constant.PRODUCT,productList.get(position));
                bundle1.putString(Constant.TRANSITION_NAME,transitionImageName);
                updateProductFragment.setArguments(bundle1);
                transitFragmentWithSharedEle(updateProductFragment,holder.ivProductImage,transitionImageName);
                //transitFragmentWithSharedElement(updateProductFragment,holder.ivProductImage,transitionImageName);
                //addNextFragment(true,updateProductFragment,holder.ivProductImage,transitionImageName);
                break;

            case 5: // Add to Cart Click

                CartItem cartItem = new CartItem(currentClickedProduct);

                if(cartAdapter.isCartAdded(cartItem)){
                    Toast.makeText(getContext(), "Item already added in the Cart", Toast.LENGTH_SHORT).show();
                }else{
                    // Add item to the Adapter
                    cartAdapter.addCartItem(cartItem);

                    setCartText(cartAdapter.getItemCount());
                }



                break;
        }*/

    }


    public int cartItemCount(){
        return cartAdapter.getItemCount();
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

    @Override
    public void removeItem() {
        setCartText(cartItemCount());

        // if All Item Remove Hide Cart View
        if(cartItemCount()==0){
            CustomAnimator.reversePrevious();
        }
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

    // Firebase Child Event Listener

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Product product = dataSnapshot.getValue(Product.class);
        adapter.addProduct(product);

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        Product product = dataSnapshot.getValue(Product.class);

        adapter.updateProduct(product);

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


}

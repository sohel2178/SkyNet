package com.imatbd.skynet.TabFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.imatbd.skynet.Adapter.OrderAdapter;
import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.Firebase.MyDatabaseReference;
import com.imatbd.skynet.Model.Order;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.NavFragments.BaseFragment;
import com.imatbd.skynet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForwardedOrderFragment extends BaseFragment implements ChildEventListener{

    private RecyclerView rvOrders;

    private OrderAdapter orderAdapter;

    private Query orderQuery;


    public ForwardedOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderAdapter = new OrderAdapter(getContext());

        User user = UserData.getInstance(getContext()).getUser();
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();

        DatabaseReference orderRef = myDatabaseReference.getOrderRef();

        switch (user.getUserType()){
            case 1:
                orderQuery = orderRef.orderByChild("adminId_and_order_state").equalTo(user.getId()+"|"+1);
                break;

            case 2:
                orderQuery = orderRef.orderByChild("agentId_and_order_state").equalTo(user.getId()+"|"+1);
                break;

            case 3:
                orderQuery = orderRef.orderByChild("customerId_and_order_state").equalTo(user.getId()+"|"+1);
                break;
        }

        if(orderQuery!=null){
            orderQuery.addChildEventListener(this);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forwarded_order, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rvOrders = view.findViewById(R.id.rv_orders);
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrders.setAdapter(orderAdapter);
    }


    @Override
    public void onDestroy() {
        orderQuery.removeEventListener(this);
        super.onDestroy();
    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Order order = dataSnapshot.getValue(Order.class);

        if(order!=null){
            orderAdapter.addOrder(order);
        }

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Order order = dataSnapshot.getValue(Order.class);

        if(order.getOrder_state()!=1){
            orderAdapter.removeItem(order);
        }



    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Order order = dataSnapshot.getValue(Order.class);
        orderAdapter.removeItem(order);

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

   /* @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Order order = dataSnapshot.getValue(Order.class);
        Log.d("JJJJJ",order.getOrder_id());
        if(order!=null){
            orderAdapter.addOrder(order);
        }

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Order order = dataSnapshot.getValue(Order.class);

        if(order!=null){
            orderAdapter.removeItem(order);
        }

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Order order = dataSnapshot.getValue(Order.class);

        Log.d("JJJJJ",order.getOrder_id());

        if(order!=null){
            orderAdapter.removeItem(order);
        }

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }*/
}

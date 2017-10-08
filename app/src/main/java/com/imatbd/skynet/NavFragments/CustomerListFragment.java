package com.imatbd.skynet.NavFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.imatbd.skynet.Adapter.UserAdapter;
import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.Firebase.MyDatabaseReference;
import com.imatbd.skynet.Listener.UserClickListener;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.R;
import com.imatbd.skynet.Utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerListFragment extends BaseFragment implements View.OnClickListener,
        ChildEventListener,UserClickListener{

    private RecyclerView rvCustomers;
    private ImageView ivAdd;

    private UserAdapter adapter;

    private Query customerQuery;




    public CustomerListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new UserAdapter(getContext());
        adapter.setListener(this);

        setUpFirebaseDatabase();
    }

    private void setUpFirebaseDatabase(){
        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        DatabaseReference userRef = myDatabaseReference.getUserRef();
        User user = UserData.getInstance(getContext()).getUser();

        customerQuery =userRef.orderByChild(Constant.AGENT_ID).equalTo(user.getId());
        customerQuery.addChildEventListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        rvCustomers = view.findViewById(R.id.rv_customers);
        ivAdd = view.findViewById(R.id.iv_add);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(Constant.CUSTOMER_LIST);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rvCustomers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCustomers.setAdapter(adapter);

        ivAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        transitFragment(new AddCustomerFragment());

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        User user = dataSnapshot.getValue(User.class);
        adapter.addUser(user);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        User user = dataSnapshot.getValue(User.class);
        adapter.updateUser(user);

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

    @Override
    public void onUserClick(User user, int clickId) {

        switch (clickId){
            case 1:
                break;

            case 2:
                shareCode(user);
                break;
        }

    }

    @Override
    public void onDestroy() {
        customerQuery.removeEventListener(this);
        super.onDestroy();
    }
}

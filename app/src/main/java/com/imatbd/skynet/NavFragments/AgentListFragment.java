package com.imatbd.skynet.NavFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.imatbd.skynet.MainActivity;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.R;
import com.imatbd.skynet.Utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgentListFragment extends BaseFragment implements ChildEventListener,
        UserClickListener{
    private ImageView ivImage;
    private RecyclerView rvAgents;

    private UserAdapter adapter;

    private Query userQuery;



    public AgentListFragment() {
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
        String adminId;
        if(user.getUserType()==1){
            adminId = user.getId();
        }else{
            adminId= user.getAdminId();
        }

        userQuery =userRef.orderByChild(Constant.ADMIN_ID).equalTo(adminId);
        userQuery.addChildEventListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agent_list, container, false);
        
        initView(view);
        return view;
    }

    private void initView(View view) {
        ivImage = view.findViewById(R.id.iv_add);
        rvAgents = view.findViewById(R.id.rv_agents);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rvAgents.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAgents.setAdapter(adapter);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitFragment(new AddAgentFragment());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        setTitle(Constant.AGENT_LIST);


    }

    @Override
    public void onDestroy() {
        userQuery.removeEventListener(this);
        super.onDestroy();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        User user = dataSnapshot.getValue(User.class);
        if(user.getUserType()==2){
            adapter.addUser(user);
        }


    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        User user = dataSnapshot.getValue(User.class);

        if(user.getUserType()==2){
            adapter.updateUser(user);
        }



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
    public void onUserClick(User user,int clickId) {

        switch (clickId){
            case 1:
                break;

            case 2:
                shareCode(user);
                break;
        }

    }


}

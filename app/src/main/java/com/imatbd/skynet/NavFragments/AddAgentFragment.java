package com.imatbd.skynet.NavFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.imatbd.skynet.AppUtility.DialogHelper;
import com.imatbd.skynet.AppUtility.MyUtils;
import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.Firebase.MyDatabaseReference;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.R;
import com.imatbd.skynet.Utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddAgentFragment extends BaseFragment implements View.OnClickListener {

    private EditText etName,etEmail,etPhone;
    private Button btnCreateAgent;

    private DatabaseReference userRef;

    private DialogHelper helper;


    public AddAgentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new DialogHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_agent, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        etName = view.findViewById(R.id.name);
        etEmail = view.findViewById(R.id.email);
        etPhone = view.findViewById(R.id.phone);
        btnCreateAgent = view.findViewById(R.id.create_agent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnCreateAgent.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        setTitle(Constant.CREATE_AGENT);
    }

    @Override
    public void onClick(View view) {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(getContext(), "Name Field is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getContext(), "Email Field is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(getContext(), "Phone Field is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!MyUtils.validateEmail(email)){
            Toast.makeText(getContext(), "Email is Not Valid", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!MyUtils.validatePhoneNumber(phone)){
            Toast.makeText(getContext(), "Phone is Not Valid", Toast.LENGTH_SHORT).show();
            return;
        }

        User currentUser = UserData.getInstance(getContext()).getUser();

        User user = new User(name,email,phone,currentUser.getUserType()+1,currentUser.getId());

        if(MyUtils.isNetworkConnected(getContext())){
            helper.showProgressDialog();
            MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
            userRef = myDatabaseReference.getUserRef();
            userRef.push().setValue(user, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    String key = databaseReference.getKey();

                    updateKey(key);
                }
            });
        }else{
            Toast.makeText(getContext(), "No internet Connection. Please Check and Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateKey(String key) {
        userRef.child(key).child("id").setValue(key);
        helper.dismisDialog();
        getFragmentManager().beginTransaction().remove(this).commit();
        getFragmentManager().popBackStack();
    }
}

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
public class AddCustomerFragment extends BaseFragment implements View.OnClickListener {
    private EditText etName,etEmail,etPhone,etShopName,etAddress;
    private Button btnCreateCustomer;

    private DatabaseReference userRef;

    private DialogHelper helper;


    public AddCustomerFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_customer, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        etName = view.findViewById(R.id.name);
        etEmail = view.findViewById(R.id.email);
        etPhone = view.findViewById(R.id.phone);
        etShopName = view.findViewById(R.id.shop_name);
        etAddress = view.findViewById(R.id.address);
        btnCreateCustomer = view.findViewById(R.id.create_agent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnCreateCustomer.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        setTitle(Constant.CREATE_CUSTOMER);

    }

    @Override
    public void onClick(View view) {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String shopName = etShopName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

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

        if(TextUtils.isEmpty(shopName)){
            Toast.makeText(getContext(), "Shop Name Field is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(address)){
            Toast.makeText(getContext(), "Address Field is Empty", Toast.LENGTH_SHORT).show();
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

        User user = new User(name,email,phone,shopName,address,currentUser.getUserType()+1,currentUser.getAdminId(),currentUser.getId());

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

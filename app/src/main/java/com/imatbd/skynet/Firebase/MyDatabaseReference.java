package com.imatbd.skynet.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Genius 03 on 7/13/2017.
 */

public class MyDatabaseReference {

    private static final String USER_REF="Users";
    private static final String PRODUCT_REF="Products";
    private static final String PURCHASE_REF="Purchase";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mainRef;


    public MyDatabaseReference() {

        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.mainRef = firebaseDatabase.getReference();

    }

    public DatabaseReference getUserRef(){
        DatabaseReference userRef = mainRef.child(USER_REF);
        return userRef;
    }

    public DatabaseReference getProductRef(){
        DatabaseReference prodRef = mainRef.child(PRODUCT_REF);
        return prodRef;
    }

    public DatabaseReference getPurchaseRef(){
        DatabaseReference purchaseRef = mainRef.child(PURCHASE_REF);
        return purchaseRef;
    }





}

package com.imatbd.skynet.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.Firebase.MyDatabaseReference;
import com.imatbd.skynet.Model.Product;
import com.imatbd.skynet.R;

public class AddProductActivity extends BaseDetailActivity implements View.OnClickListener{

    private EditText etProdName,etProdDescription,etProdPrice;
    private Button btnAddProduct;

    private DatabaseReference prodRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        prodRef = myDatabaseReference.getProductRef();

        windowFadeAnimation();

        setupToolbar();

        initView();
    }

    private void initView() {

        etProdName = (EditText) findViewById(R.id.prodName);
        etProdDescription = (EditText) findViewById(R.id.prodDescription);
        etProdPrice = (EditText) findViewById(R.id.prodPrice);

        btnAddProduct = (Button) findViewById(R.id.btn_add_product);
        btnAddProduct.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String prodName = etProdName.getText().toString().trim();
        String prodDesc = etProdDescription.getText().toString().trim();
        String prodPrice = etProdPrice.getText().toString().trim();

        Product product = new Product(prodName,prodDesc,Double.parseDouble(prodPrice),
                UserData.getInstance(getApplicationContext()).getUser().getId());

        prodRef.push().setValue(product, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String key = databaseReference.getKey();

                updateKey(key);
            }
        });

    }

    private void updateKey(String key) {
        prodRef.child(key).child("id").setValue(key);
        finish();
    }
}

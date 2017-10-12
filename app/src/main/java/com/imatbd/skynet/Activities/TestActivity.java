package com.imatbd.skynet.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.imatbd.skynet.Model.CartItem;
import com.imatbd.skynet.R;
import com.imatbd.skynet.ViewModel.MyLayout;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private MyLayout myLayout;

    private List<CartItem> cartItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        myLayout = findViewById(R.id.myTable);

        cartItemList = (List<CartItem>) getIntent().getSerializableExtra("data");

        if(cartItemList!=null){
            myLayout.setCartItemListList(cartItemList);
        }


    }
}

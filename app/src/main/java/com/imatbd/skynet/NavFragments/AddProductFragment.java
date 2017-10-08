package com.imatbd.skynet.NavFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.imatbd.skynet.AppUtility.MyUtils;
import com.imatbd.skynet.Firebase.FdatabaseUtil;
import com.imatbd.skynet.Model.Product;
import com.imatbd.skynet.Model.Purchase;
import com.imatbd.skynet.R;
import com.imatbd.skynet.Utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductFragment extends BaseFragment implements View.OnClickListener {

    private Product product;

    private EditText etBuyFrom,etUnitPrice,etQuantity;
    private Button btnAddQuantity;


    public AddProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            product = (Product) getArguments().getSerializable(Constant.PRODUCT);

            Log.d("JJJ","Product Found");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        etBuyFrom = view.findViewById(R.id.buy_from);
        etUnitPrice = view.findViewById(R.id.unit_price);
        etQuantity = view.findViewById(R.id.quantity);
        btnAddQuantity = view.findViewById(R.id.add_product);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnAddQuantity.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(product!=null){
            setTitle(product.getName());
        }
    }

    @Override
    public void onClick(View view) {
        MyUtils.hideKey(view);
        String buyFrom = etBuyFrom.getText().toString().trim();
        String unitPriceStr = etUnitPrice.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();


        if(TextUtils.isEmpty(buyFrom)){
            Toast.makeText(getContext(), "Buy From Filed is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(unitPriceStr)){
            Toast.makeText(getContext(), "Unit Price Filed is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(quantityStr)){
            Toast.makeText(getContext(), "Quantity Filed is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Purchase purchase = new Purchase(product.getId(),getUserId(),buyFrom,Double.parseDouble(unitPriceStr),
                Integer.parseInt(quantityStr));

        if(isNetworkConnected()){
            FdatabaseUtil fdatabaseUtil = new FdatabaseUtil(getContext());
            fdatabaseUtil.addPurchase(purchase);
            fdatabaseUtil.setIdUpdateListener(new FdatabaseUtil.IdUpdateListener() {
                @Override
                public void updateId(String id) {
                    getFragmentManager().popBackStack();
                }
            });

        }else{
            Toast.makeText(getContext(), "Please Turn On Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }
}

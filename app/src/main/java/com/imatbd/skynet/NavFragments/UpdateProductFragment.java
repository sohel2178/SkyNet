package com.imatbd.skynet.NavFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.Model.Product;
import com.imatbd.skynet.R;
import com.imatbd.skynet.Utility.Constant;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProductFragment extends BaseFragment implements View.OnClickListener {

    private Product product;

    private ImageView ivProduct;
    private EditText etName,etDesc,etAvailableQuantity;
    private Button btnUpdate;

    String trName;




    public UpdateProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            product = (Product) getArguments().getSerializable(Constant.PRODUCT);
            trName = getArguments().getString(Constant.TRANSITION_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_product, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {

        ivProduct = view.findViewById(R.id.product_image);
        ivProduct.setTransitionName(trName);
        etName = view.findViewById(R.id.prodName);
        etDesc = view.findViewById(R.id.prodDesc);
        etAvailableQuantity = view.findViewById(R.id.availableQuantity);
        btnUpdate = view.findViewById(R.id.update);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bindData();

        btnUpdate.setOnClickListener(this);
    }

    private void bindData() {
        if(product!=null){

            if(!product.getImageUrl().equals("")){
                Picasso.with(getContext())
                        .load(product.getImageUrl())
                        .into(ivProduct);
            }else{
                ivProduct.setImageResource(R.drawable.product_image);
            }

            etName.setText(product.getName());
            etDesc.setText(product.getDesciption());
            etAvailableQuantity.setText(String.valueOf(product.getAvailableQuantity()));

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        setTitle("Update "+product.getName());

        if(getUserType()==1){

            Log.d("GGGG","Inside Test");
            etName.setEnabled(true);
            etDesc.setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.equals(btnUpdate)){

        }
    }
}

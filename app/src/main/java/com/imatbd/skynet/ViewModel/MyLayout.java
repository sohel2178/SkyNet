package com.imatbd.skynet.ViewModel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.imatbd.skynet.Model.CartItem;
import com.imatbd.skynet.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Genius 03 on 10/12/2017.
 */

public class MyLayout extends TableLayout {
    private Context context;

    private List<CartItem> cartItemList;

    private List<String> headerList = new ArrayList<>();

    private double total;

    public MyLayout(Context context) {
        super(context);
        init(context);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        initHeaderList();
    }

    private void drawHeader(){
        TableRow tr_head = new TableRow(context);
        tr_head.setBackgroundResource(R.drawable.header_back);       // part1
        tr_head.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        tr_head.setPadding(20,20,20,20);

        initHeader(tr_head);

        this.addView(tr_head,0);

    }


    private void drawCartItems(){

        for(int i=0 ; i<cartItemList.size();i++){

            TableRow tr = new TableRow(context);
            tr.setBackgroundColor(Color.parseColor("#E0E0E0"));        // part1
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            tr.setPadding(20,20,20,20);

            showTableData(i,tr,cartItemList.get(i));

            this.addView(tr,i+1);// i+1 because of Header Already Exist
        }

    }

    private void drawTotalRow(double value){
        TableRow tr = new TableRow(context);
        tr.setBackgroundResource(R.drawable.footer_back);       // part1
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        tr.setPadding(20,20,20,20);

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 4;

        TextView tvTotal = new TextView(context);
        tvTotal.setLayoutParams(params);
        tvTotal.setGravity(Gravity.CENTER);
        tvTotal.setTextColor(ContextCompat.getColor(getContext(),R.color.text_light));
        tvTotal.setText("Total");

        TextView totalValue = new TextView(context);
        totalValue.setGravity(Gravity.END);
        totalValue.setTextColor(ContextCompat.getColor(getContext(),R.color.text_light));
        totalValue.setPadding(0,0,60,0);
        totalValue.setText(twoDecimalPlace(value));
        totalValue.setTypeface(totalValue.getTypeface(), Typeface.BOLD);

        tr.addView(tvTotal,0);
        tr.addView(totalValue,1);

        this.addView(tr);
    }

    private void showTableData(int i, TableRow tr, CartItem cartItem) {
        tr.setBackgroundResource(R.drawable.cell_back);
        TextView tvSl = new TextView(context);
        tvSl.setTypeface(tvSl.getTypeface(), Typeface.BOLD);
        tvSl.setText(String.valueOf(i+1));

        TextView tvName = new TextView(context);
        tvName.setText(cartItem.getProduct().getName());

        TextView tvQuantity = new TextView(context);
        tvQuantity.setGravity(Gravity.END);
        tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

        TextView tvPrice = new TextView(context);
        tvPrice.setGravity(Gravity.END);
        tvPrice.setText(twoDecimalPlace(cartItem.getProduct().getPrice()));

        TextView tvAmount = new TextView(context);
        tvAmount.setPadding(0,0,60,0);
        tvAmount.setGravity(Gravity.END);
        tvAmount.setTypeface(tvAmount.getTypeface(), Typeface.BOLD);
        tvAmount.setText(twoDecimalPlace(cartItem.getProduct().getPrice()*cartItem.getQuantity()));

        total = total+cartItem.getProduct().getPrice()*cartItem.getQuantity();

        tr.addView(tvSl,0);
        tr.addView(tvName,1);
        tr.addView(tvQuantity,2);
        tr.addView(tvPrice,3);
        tr.addView(tvAmount,4);

    }

    public void setCartItemListList(List<CartItem> cartItemList){
        Log.d("HHH","setProductList Called");
        this.cartItemList =cartItemList;
         if(cartItemList!= null){

             if(cartItemList.size()>0){
                 drawHeader();
                 drawCartItems();
                 drawTotalRow(total);
             }


        }
    }

    public void resetCartList(){
        this.cartItemList = new ArrayList<>();
        removeAllViews();

    }




    private void initHeader(TableRow tr) {
        for(int i=0;i<headerList.size();i++){
            TextView txtSlNo = new TextView(context);
            txtSlNo.setTextColor(ContextCompat.getColor(getContext(),R.color.text_light));
            txtSlNo.setTypeface(Typeface.DEFAULT_BOLD);
            if(i>1){
                txtSlNo.setGravity(Gravity.END);
            }

            if(i==(headerList.size()-1)){
                txtSlNo.setPadding(0,0,60,0);

            }
            txtSlNo.setText(headerList.get(i));
            tr.addView(txtSlNo,i);
        }
    }

    private void initHeaderList() {
        headerList.add("Sl No");
        headerList.add("Product");
        headerList.add("Quantity");
        headerList.add("Price");
        headerList.add("Amount");
    }

    private String twoDecimalPlace(double value){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value);
    }
}

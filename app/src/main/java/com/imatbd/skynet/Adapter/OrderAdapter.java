package com.imatbd.skynet.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imatbd.skynet.Model.Order;
import com.imatbd.skynet.R;
import com.imatbd.skynet.ViewModel.MyLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Genius 03 on 10/12/2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    private Context context;

    private List<Order> orderList;
    private LayoutInflater inflater;

    private int clickedPosition;

    public OrderAdapter(Context context) {
        this.context = context;
        this.orderList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);

        this.clickedPosition =-1;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_order,parent,false);

        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {

        Order order = orderList.get(position);
        holder.tvDate.setText(getDate(order.getOrder_time()));
        holder.bottomContainer.setVisibility(View.GONE);

        if(position==clickedPosition){
            // reset before Click
            holder.myLayout.resetCartList();
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_from_top);

            holder.myLayout.setCartItemListList(order.getCart_item_list());
            //toggling visibility
            holder.bottomContainer.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.bottomContainer.startAnimation(slideDown);
        }else{
            holder.myLayout.resetCartList();
        }



    }

    public void addOrder(Order order){
        orderList.add(order);
        int position = orderList.indexOf(order);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvCustomerName,tvAddress,tvDate;
        MyLayout myLayout;
        Button btnCancel,btnAccept;
        RelativeLayout bottomContainer;

        public OrderHolder(View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.customer_name);
            tvAddress = itemView.findViewById(R.id.customer_address);
            tvDate = itemView.findViewById(R.id.order_date);
            myLayout = itemView.findViewById(R.id.my_layout);
            btnCancel = itemView.findViewById(R.id.cancel);
            btnAccept = itemView.findViewById(R.id.accept);
            bottomContainer = itemView.findViewById(R.id.bottom_container);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickedPosition = getAdapterPosition();
            notifyDataSetChanged();

        }
    }

    private String getDate(long time){
        Date date = new Date(time);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMM yyyy");

        return dateFormat.format(date);
    }
}

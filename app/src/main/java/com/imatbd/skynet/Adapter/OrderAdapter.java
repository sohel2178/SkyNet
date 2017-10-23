package com.imatbd.skynet.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.Firebase.MyDatabaseReference;
import com.imatbd.skynet.Model.Order;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.R;
import com.imatbd.skynet.ViewModel.MyLayout;
import com.imatbd.skynet.Volley.NotificationSender;

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

    private DatabaseReference customerRef;
    private DatabaseReference orderRef;

    public OrderAdapter(Context context) {
        this.context = context;
        this.orderList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);

        MyDatabaseReference myDatabaseReference = new MyDatabaseReference();
        customerRef = myDatabaseReference.getUserRef();
        orderRef = myDatabaseReference.getOrderRef();

        this.clickedPosition =-1;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_order,parent,false);

        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderHolder holder, int position) {

        Order order = orderList.get(position);
        holder.tvDate.setText(getDate(order.getOrder_time()));
        holder.bottomContainer.setVisibility(View.GONE);

        customerRef.child(order.getCustomerId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if(user!=null){
                    holder.tvCustomerName.setText(user.getName());
                    holder.tvAddress.setText(user.getAddress());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        orderList.add(0,order);
        notifyItemInserted(0);
    }

    public void removeItem(Order order){
        int position = getPosition(order.getOrder_id());

        Log.d("JJJJJ",position+"");

        if(position!=-1){
            orderList.remove(position);
            notifyItemRemoved(position);
        }


    }

    private int getPosition(String orderId){
        int position =-1;

        for(Order x:orderList){
            if(x.getOrder_id().equals(orderId)){
                position = orderList.indexOf(x);
                break;
            }
        }

        return position;
    }

    public void cancelOrder(final Order order){
        DatabaseReference ref = orderRef.child(order.getOrder_id());

        final String orderId = order.getOrder_id();
        final String customerId = order.getCustomerId();
        final String adminId = order.getAdminId();

        order.setOrder_state(4);
        order.setAgentId_and_order_state(order.getAgentId()+"|"+order.getOrder_state());
        order.setCustomerId_and_order_state(order.getCustomerId()+"|"+order.getOrder_state());
        order.setAdminId_and_order_state(order.getAdminId()+"|"+order.getOrder_state());

        ref.setValue(order, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                NotificationSender notificationSender = new NotificationSender(context);
                notificationSender.sendCustomNotification(orderId,customerId,"Order Request","Your order Canceled");
            }
        });
    }

    public void acceptOrder(final Order order){
        //Change order status in the Database
        DatabaseReference ref = orderRef.child(order.getOrder_id());

        final String orderId = order.getOrder_id();
        final String customerId = order.getCustomerId();
        final String adminId = order.getAdminId();

        order.setOrder_state(1);
        order.setAgentId_and_order_state(order.getAgentId()+"|"+order.getOrder_state());
        order.setCustomerId_and_order_state(order.getCustomerId()+"|"+order.getOrder_state());
        order.setAdminId_and_order_state(order.getAdminId()+"|"+order.getOrder_state());

        ref.setValue(order, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                NotificationSender notificationSender = new NotificationSender(context);
                notificationSender.sendCustomNotification(orderId,customerId,"Order Request","Your order is Accepted");
                notificationSender.sendCustomNotification(orderId,adminId,"Order Request","You have a New Order");
            }
        });

        //Remove Item form Adapter

        // Send Notification to Customer
    }




    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvCustomerName,tvAddress,tvDate;
        MyLayout myLayout;
        Button btnCancel,btnAccept,btnApprove;
        RelativeLayout bottomContainer;

        public OrderHolder(View itemView) {
            super(itemView);
            User user = UserData.getInstance(context).getUser();
            tvCustomerName = itemView.findViewById(R.id.customer_name);
            tvAddress = itemView.findViewById(R.id.customer_address);
            tvDate = itemView.findViewById(R.id.order_date);
            myLayout = itemView.findViewById(R.id.my_layout);
            btnCancel = itemView.findViewById(R.id.cancel);
            btnAccept = itemView.findViewById(R.id.accept);
            btnApprove = itemView.findViewById(R.id.approve);
            bottomContainer = itemView.findViewById(R.id.bottom_container);

            if(user.getUserType()==3){
                btnAccept.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
            }

            if(user.getUserType()==1){
                btnAccept.setVisibility(View.GONE);
                btnApprove.setVisibility(View.VISIBLE);
            }


            btnAccept.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
            btnApprove.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view.equals(btnCancel)){
                cancelOrder(orderList.get(getAdapterPosition()));
            }else if(view.equals(btnAccept)){
                acceptOrder(orderList.get(getAdapterPosition()));

            }else if(view.equals(btnApprove)){
                approve(orderList.get(getAdapterPosition()));

            } else if(view.equals(itemView)){
                clickedPosition = getAdapterPosition();
                notifyDataSetChanged();
            }



        }
    }

    private void approve(Order order){
        //Change order status in the Database
        DatabaseReference ref = orderRef.child(order.getOrder_id());

        final String orderId = order.getOrder_id();
        final String customerId = order.getCustomerId();
        final String agentId = order.getAgentId();

        order.setOrder_state(2);
        order.setAgentId_and_order_state(order.getAgentId()+"|"+order.getOrder_state());
        order.setCustomerId_and_order_state(order.getCustomerId()+"|"+order.getOrder_state());
        order.setAdminId_and_order_state(order.getAdminId()+"|"+order.getOrder_state());

        ref.setValue(order, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                NotificationSender notificationSender = new NotificationSender(context);
                notificationSender.sendCustomNotification(orderId,customerId,"Order Request","Your order is in Deliver state");
                notificationSender.sendCustomNotification(orderId,agentId,"Order Request","Your Customer Order Request is in under Process");
            }
        });

        //Remove Item form Adapter

        // Send Notification to Customer

    }

    private String getDate(long time){
        Date date = new Date(time);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMM yyyy");

        return dateFormat.format(date);
    }
}

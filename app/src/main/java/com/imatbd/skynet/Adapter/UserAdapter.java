package com.imatbd.skynet.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imatbd.skynet.Listener.UserClickListener;
import com.imatbd.skynet.Model.Product;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Genius 03 on 10/4/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private Context context;
    private List<User> userList;
    private LayoutInflater inflater;

    private UserClickListener listener;

    public UserAdapter(Context context) {
        this.context = context;
        this.userList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_user,parent,false);
        UserHolder holder = new UserHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        User user = userList.get(position);

        holder.tvName.setText(user.getName());
        holder.tvPhone.setText(user.getPhone());

        if(!user.getImageUrl().equals("")){
            Picasso.with(context)
                    .load(user.getImageUrl())
                    .into(holder.ivImage);
        }

    }

    public void addUser(User user){
        userList.add(user);

        int position = userList.indexOf(user);

        notifyItemInserted(position);
    }

    public void updateUser(User user){

        int position = getPosition(user);

        if(position!=-1){
            userList.set(position,user);
            notifyItemChanged(position);
        }

    }

    private int getPosition(User user){
        int position = -1;

        for (int i=0;i<userList.size();i++){
            if(userList.get(i).getCreatedDate()==(user.getCreatedDate())){
                position=i;
                break;
            }
        }

        return position;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setListener(UserClickListener listener){
        this.listener = listener;
    }


    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvName,tvPhone;

        ImageView ivImage,ivShare;


        public UserHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            ivImage = itemView.findViewById(R.id.iv_image);
            ivShare = itemView.findViewById(R.id.share);

            ivShare.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view.equals(ivShare)){
                if(listener!=null){
                    listener.onUserClick(userList.get(getAdapterPosition()),2);
                }
            }else if(view.equals(itemView)){
                if(listener!=null){
                    listener.onUserClick(userList.get(getAdapterPosition()),1);
                }
            }





        }
    }
}

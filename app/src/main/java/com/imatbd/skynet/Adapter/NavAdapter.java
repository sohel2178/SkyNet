package com.imatbd.skynet.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.Listener.NavItemClickListener;
import com.imatbd.skynet.Model.NavData;
import com.imatbd.skynet.R;
import com.imatbd.skynet.Utility.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Genius 03 on 10/2/2017.
 */

public class NavAdapter extends RecyclerView.Adapter<NavAdapter.NavHolder> {

    private Context context;
    private List<NavData> navDataList;
    private LayoutInflater inflater;

    private NavItemClickListener listener;

    public NavAdapter(Context context) {
        this.context = context;
        initList(UserData.getInstance(context).getUser().getUserType());
        this.inflater = LayoutInflater.from(context);
    }

    public void setListener(NavItemClickListener listener){
        this.listener = listener;
    }

    private void initList(int userType) {
        navDataList = new ArrayList<>();
        navDataList.add(new NavData(0, Constant.HOME, R.drawable.home));
        navDataList.add(new NavData(0, Constant.PRODUCTS, R.drawable.products));

        switch (userType){
            case 1:
                navDataList.add(new NavData(0,Constant.AGENT_LIST, R.drawable.agents));
                break;

            case 2:
                navDataList.add(new NavData(0,Constant.CUSTOMER_LIST, R.drawable.agents));
                break;

            case 3:
                break;
        }
    }

    @Override
    public NavHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_nav_item,parent,false);

        NavHolder holder = new NavHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(NavHolder holder, int position) {
        NavData data = navDataList.get(position);

        holder.ivIcon.setImageResource(data.getResId());
        holder.tvName.setText(data.getName());

    }

    @Override
    public int getItemCount() {
        return navDataList.size();
    }

    public class NavHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvName;
        ImageView ivIcon;

        public NavHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            ivIcon = itemView.findViewById(R.id.icon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(listener != null){
                listener.onItemClick(navDataList.get(getAdapterPosition()));
            }

        }
    }
}

package com.imatbd.skynet.Listener;

import com.imatbd.skynet.Model.User;

/**
 * Created by Genius 03 on 10/4/2017.
 */

public interface UserClickListener {

    // itemView ==1
    // share ==2
    public void onUserClick(User user,int clickId);
}

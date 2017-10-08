package com.imatbd.skynet.Utility;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Genius 03 on 8/28/2017.
 */

public class TransitionHelper {

    public static Pair<View,String>[] createSafeTransitionParticipants(@NonNull Activity activity,
                                                                       boolean includeStatusBar, @Nullable Pair... otherParticipants){

        View decor = activity.getWindow().getDecorView();
        View statusBar = null;

        if(includeStatusBar){
            statusBar = decor.findViewById(android.R.id.statusBarBackground);
        }

        View navBar = decor.findViewById(android.R.id.navigationBarBackground);

        List<Pair> participantList = new ArrayList<>(3);

        addNonNullViewToTransitionParticipants(statusBar, participantList);
        addNonNullViewToTransitionParticipants(navBar, participantList);

        if(otherParticipants!=null && !(otherParticipants.length==1 && otherParticipants[0]==null)){
            participantList.addAll(Arrays.asList(otherParticipants));
        }

        return participantList.toArray(new Pair[participantList.size()]);

    }

    private static void addNonNullViewToTransitionParticipants(View view, List<Pair> participantList) {
        if(view==null){
            return;
        }

        participantList.add(new Pair<>(view,view.getTransitionName()));
    }
}

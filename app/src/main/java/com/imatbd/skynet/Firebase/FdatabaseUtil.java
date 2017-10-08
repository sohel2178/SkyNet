package com.imatbd.skynet.Firebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.imatbd.skynet.AppUtility.UserData;
import com.imatbd.skynet.AppUtility.UserLocalStore;
import com.imatbd.skynet.Model.Purchase;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.Utility.Method;

/**
 * Created by Genius 03 on 9/26/2017.
 */

public class FdatabaseUtil {

    private MyDatabaseReference myDatabaseReference;
    private Context context;
    private UserLocalStore userLocalStore;

    private UserListener listener;

    private IdUpdateListener idUpdateListener;

    public FdatabaseUtil(Context context) {
        this.context = context;
        myDatabaseReference = new MyDatabaseReference();
        userLocalStore = UserLocalStore.getInstance(context);
    }

    public void addAdmin(User user){

        myDatabaseReference.getUserRef().push().setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String id = databaseReference.getKey();
                updateUserId(id);

            }
        });

    }

    public void addPurchase(Purchase purchase){
        myDatabaseReference.getPurchaseRef().push().setValue(purchase, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String id = databaseReference.getKey();
                updatePurchaseKey(id);
            }
        });
    }

    private void updatePurchaseKey(final String id) {
        myDatabaseReference.getPurchaseRef().child(id).child("id").setValue(id, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(idUpdateListener!=null){
                    idUpdateListener.updateId(databaseReference.getKey());
                }
            }
        });
    }


    public void setListener(UserListener listener){
        this.listener =listener;
    }

    public void setIdUpdateListener(IdUpdateListener idUpdateListener){
        this.idUpdateListener =idUpdateListener;
    }

    private void updateUserId(String id){
        myDatabaseReference.getUserRef().child(id).child("id").setValue(id);
    }

    public void query(String id){
        Query query = myDatabaseReference.getUserRef().child(id).equalTo("Sajib Biswas");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //User user = dataSnapshot.getValue(User.class);

                Log.d("SOHEL",dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateProfileImage(String url){
        String id = UserData.getInstance(context).getUser().getId();
        Log.d("ID",id);
        if(!id.equals("")){
            Log.d("ID",id+" inside if");
            myDatabaseReference.getUserRef().child(id).child("imageUrl").setValue(url);
        }

    }

    public void updateProductImage(String url,String id){

        myDatabaseReference.getProductRef().child(id).child("imageUrl").setValue(url);

    }

    public void getUserByID(String id){

        myDatabaseReference.getUserRef().child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if(listener!=null){
                    listener.getUser(user, Method.FETCH_USER);
                }
                Log.d("SOHEL",dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void updateToken(final User user){
        myDatabaseReference.getUserRef().child(user.getId()).child("token").setValue(userLocalStore.getToken(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(listener!=null){
                    listener.getUser(null,Method.UPDATE_TOKEN);
                    // Update User as Registered
                    userLocalStore.setRegister(true);

                    user.setToken(userLocalStore.getToken());

                    // Set UserData(in the Preference)
                    UserData.getInstance(context).saveUser(user);
                }
            }
        });
    }


    public interface UserListener{
        public void getUser(User user,int methodId);
    }

    public interface IdUpdateListener {

        public void updateId(String id);
    }


}

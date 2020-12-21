package com.example.juchawhich;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

interface OnInfomationFilledListener {
    void onInfomationFilled(ArrayList<friend_Infomation> infomation);
    void onError(Exception taskException);
}
interface OnFriendFilledListener {
    void onfriendFilled(friend_Infomation infomation);
    void onError(Exception taskException);
}
class friend_Infomation{
    public Boolean allow;
    public String name;
    public String uid;
    public int number;
    public String email;
}

public class newfriend_Func {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth user = FirebaseAuth.getInstance();

    public void add_Friend_by_info(final String myuid, String friend_uid, String name, String email){
        final friend_Infomation friends = new friend_Infomation();
        friends.allow = false;
        friends.name = name;
        friends.number = 0;
        friends.uid = friend_uid;
        friends.email = email;

        db.collection("friends")
                .document(myuid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        List list = (List) document.getData().get("list");
                        if(list != null) {
                            db.collection("friends")
                                    .document(myuid)
                                    .update("list", FieldValue.arrayUnion(friends));
                        }else{
                            db.collection("friends")
                                    .document(myuid)
                                    .update("list", FieldValue.arrayUnion(friends));
                        }
                    }else{
                        HashMap<String, Object> hash = new HashMap<>();
                        hash.put("dump", null);
                        db.collection("friends")
                                .document(myuid).set(hash);
                        db.collection("friends")
                                .document(myuid).update("dump", FieldValue.delete());
                        db.collection("friends")
                                .document(myuid)
                                .update("list", FieldValue.arrayUnion(friends));
                    }
                }
            }
        });

    }

    public void load_Friend_list1(String myuid, final OnInfomationFilledListener listener){
        db.collection("friends").document(myuid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<friend_Infomation> friendArray = new ArrayList<>();
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        List list = (List) document.getData().get("list");
                        if(list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                HashMap map;
                                map = (HashMap<String, Object>) list.get(i);
                                friend_Infomation temp = new friend_Infomation();
                                temp.name = map.get("name").toString();
                                temp.number = i;
                                temp.uid = map.get("uid").toString();
                                temp.allow = map.get("allow").equals(true);
                                temp.email = map.get("email").toString();
                                friendArray.add(i, temp);
                            }
                            listener.onInfomationFilled(friendArray);
                        }
                        else{
                            listener.onInfomationFilled(null);
                        }

                    }else{
                        listener.onInfomationFilled(null);
                    }
                }else {
                    listener.onError(task.getException());
                }
            }
        });
    }

    public void load_Friend_by_number(String myuid, final int number, final OnFriendFilledListener listener){
        db.collection("friends").document(myuid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    List list = (List) document.getData().get("list");
                    HashMap map;
                    map = (HashMap<String, Object>) list.get(number);
                    friend_Infomation temp = new friend_Infomation();
                    temp.name = map.get("name").toString();
                    temp.uid = map.get("uid").toString();
                    temp.allow = map.get("allow").equals(true);
                    temp.email = map.get("email").toString();

                    listener.onfriendFilled(temp);
                }else {
                    listener.onError(task.getException());
                }
            }
        });
    }
    public void load_Friend_by_name(String name, final  OnFriendFilledListener listener){
        db.collection("user_info").document(name)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    friend_Infomation friend_infomation = new friend_Infomation();
                    friend_infomation.email = document.getString("email");
                    friend_infomation.name = document.getString("name");
                    friend_infomation.uid = document.getString("uid");

                    listener.onfriendFilled(friend_infomation);
                }else{
                    listener.onError(task.getException());
                }
            }
        });
    }

    public void update_Friend_Allow(final String myuid, final int number, final Boolean allow){

        load_Friend_list1(myuid, new OnInfomationFilledListener() {
            @Override
            public void onInfomationFilled(ArrayList<friend_Infomation> infomation) {
                for(int i = 0; i < infomation.size(); i++) {
                    if(i == number)
                        infomation.get(i).allow = allow;
                }
                db.collection("friends").document(myuid).update("list", infomation);
            }
            @Override
            public void onError(Exception taskException) {
            }
        });

    }
    public void load_Block_Friend_list(String myuid, final OnInfomationFilledListener listener){
        db.collection("friends").document(myuid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<friend_Infomation> friendArray = new ArrayList<>();
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        List list = (List) document.getData().get("blocklist");
                        if(list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                HashMap map;
                                map = (HashMap<String, Object>) list.get(i);
                                friend_Infomation temp = new friend_Infomation();
                                temp.name = map.get("name").toString();
                                temp.number = i;
                                temp.uid = map.get("uid").toString();
                                temp.allow = map.get("allow").equals(true);
                                temp.email = map.get("email").toString();
                                friendArray.add(i, temp);
                            }
                            listener.onInfomationFilled(friendArray);
                        }
                        else{
                            listener.onInfomationFilled(null);
                        }
                    }else{
                        listener.onInfomationFilled(null);
                    }
                }else {
                    listener.onError(task.getException());
                }
            }
        });
    }

    public void remove_Friend(final String myuid , final int number){
        load_Friend_list1(myuid, new OnInfomationFilledListener() {
            @Override
            public void onInfomationFilled(ArrayList<friend_Infomation> infomation) {
                infomation.remove(number);
                db.collection("friends").document(myuid).update("list", infomation);
            }
            @Override
            public void onError(Exception taskException) {
            }
        });
    }
}

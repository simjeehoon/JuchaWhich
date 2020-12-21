package com.example.juchawhich;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingFirebase {
    private volatile static ParkingFirebase instance;
    private ParkingFirebase(){
    }
    public static ParkingFirebase getInstance(){
        if(instance == null){
            synchronized (ParkingFirebase.class) {
                if(instance == null)
                    instance = new ParkingFirebase();
            }
        }
        return instance;
    }



    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "newparkingclass";

    public user_Info current_User_info(){
        user_Info user_info = new user_Info();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //이름
            user_info.name = user.getDisplayName();
            //이메일
            user_info.email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            boolean emailVerified = user.isEmailVerified();
            user_info.uid = user.getUid();
        }
        return user_info;
    }

    public void current_User_info_save(){
        user_Info user_info = new user_Info();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Map<String, Object>hash = new HashMap<>();
            hash.put("allow", false);
            hash.put("number", 0);
            hash.put("name",user.getDisplayName());
            hash.put("email",user.getEmail());
            hash.put("uid",user.getUid());
            db.collection("user_info").document(user.getDisplayName()).set(hash);
        }
    }

    public void user_Info_save(String myuid) {//user생성할때 저장해주세요
        Map<String, Object>hash1 = new HashMap<>();
        hash1.put("uid", myuid);
        db.collection("users")
                .document(myuid)
                .set(hash1);
    }

    public void parking_Info_save(String myuid, String car, GeoPoint a, String memo){//주차위치정보 저장
        Map<String, Object> hash1 = new HashMap<>();
        hash1.put("지도", a);
        hash1.put("메모", memo);
        hash1.put("시간", new Timestamp(new Date()));
        hash1.put("allow", true);
        db.collection("users")
                .document(myuid)
                .collection(car)
                .add(hash1)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void load_Parking_place(final OnParkingFilledListener listener){
        Log.d(TAG, "aaaaaa");
        db.collection("parking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){//리스트 끝날때까지
                            ArrayList<parking_Place> parkingPlaceArray = new ArrayList<>();
                            int i = 0;
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, "aaaaaa");
                                parking_Place temp = new parking_Place();
                                temp.rest = null;
                                //temp.rest
                                temp.name = document.getId();
                                temp.place = document.getGeoPoint("위치");
                                temp.price = document.get("요금");
                                Log.d(TAG, "vv");
                                parkingPlaceArray.add(i, temp);
                                //전부
                                //parkingplaceArray.get(i).rest = document.get("여유공간");
                                i++;
                            }
                            listener.onParkingFilled(parkingPlaceArray);
                        }else{
                            listener.onError(task.getException());
                        }
                    }
                });
    }

    public void load_user_Parking_place_by_parkingId(String uid, String car, String parkingId,
                                                     final OnUserParkingFilledListener listener){
        db.collection("users")
                .document(uid)
                .collection(car)
                .document(parkingId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.d(TAG, "vv");
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    userParkingInfo user_Info = new userParkingInfo();
                    document.getData();//전부
                    user_Info.time = document.getDate("시간");//필드 이름 적으셔야 합니다.
                    user_Info.place = document.getGeoPoint("지도");
                    user_Info.memo = document.getString("메모");
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    Log.d(TAG, "aaaaaa");

                    listener.onUserInfoFilled(user_Info);
                } else {
                    listener.onError(task.getException());
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void load_user_car_parking(String uid, String car, final OnUserParkingFilledListener listener){
        db.collection("users")
                .document(uid)
                .collection(car)
                .orderBy("시간", Query.Direction.DESCENDING)//시간순으로 정렬
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            int i = 0;
                            userParkingInfo temp = new userParkingInfo();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                temp.memo = document.getString("메모");
                                temp.place = document.getGeoPoint("지도");
                                temp.time = document.getDate("시간");
                                temp.allow = document.getBoolean("allow");
                                temp.parkingid = document.getId();
                                document.getData();//전부
                                break;
                            }
                            listener.onUserInfoFilled(temp);
                        }else{
                            listener.onError(task.getException());
                        }
                    }
                });
    }

    public void load_car_list(String uid, final OnCarlistFilledListener listener) {
        db.collection("users").document(uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<carInfo> cararray = new ArrayList<>();
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        if ((List) document.getData().get("carname") != null) {
                            List list = (List) document.getData().get("carname");
                            for (int i = 0; i < list.size(); i++) {
                                HashMap map;
                                map = (HashMap<String, Object>) list.get(i);
                                carInfo temp = new carInfo();
                                if (map.get("carname") == null) {
                                    temp.carname = "none";
                                } else {
                                    temp.carname = map.get("carname").toString();
                                }
                                temp.number = i;
                                if (map.get("carnum") == null) {
                                    temp.carnum = "없음";
                                } else {
                                    temp.carnum = map.get("carnum").toString();
                                }
                                if (map.get("memo") == null) {
                                    temp.memo = "없음";
                                } else {
                                    temp.memo = map.get("memo").toString();
                                }
                                if (map.get("fuel") == null) {
                                    temp.fuel = "선택 안함";
                                } else {
                                    temp.fuel = map.get("fuel").toString();
                                }
                                cararray.add(temp);
                            }
                        }
                        listener.onCarlistFilled(cararray);
                    }else{
                        listener.onCarlistFilled(null);
                    }
                }else{
                    listener.onError(task.getException());
                }
            }
        });
    }
    public void update_user_car_no_parked(final String uid, final String car){
        load_user_Parking_place_list(uid, car, new OnUserParkinglistFilledListener() {
            @Override
            public void onUserParkinglistFilled(ArrayList<userParkingInfo> infomation) {
                db.collection("users")
                        .document(uid)
                        .collection(car).document(infomation.get(0).parkingid).update("allow", false);
            }

            @Override
            public void onError(Exception taskException) {

            }
        });
    }
    public void load_parking_state(String uid, String car, final OnBooleanListener listener){
        Log.d("test1111", "aaaa");
        load_user_car_parking(uid, car, new OnUserParkingFilledListener() {
            @Override
            public void onUserInfoFilled(userParkingInfo infomation) {
                listener.onBooleanFilled(infomation.allow);
            }
            @Override
            public void onError(Exception taskException) {
            }
        });
        Log.d("test1111", "bbbb");
    }

    public void add_car(String uid ,String carname, String carnum, String fuel, String memo){
        carInfo carinfo = new carInfo();
        carinfo.carname = carname;
        carinfo.carnum = carnum;
        carinfo.fuel = fuel;
        carinfo.memo = memo;
        carinfo.number = 0;
        DocumentReference carRef = db.collection("users").document(uid);
        carRef.update("carname", FieldValue.arrayUnion(carinfo));
    }


    /*
    public void remove_car(final String myuid , final int number){//주차위치정보 저장
        load_car_list(user.getUid(), new OnCarlistFilledListener() {
            @Override
            public void onCarlistFilled(ArrayList<carInfo> infomation) {
                infomation.remove(number);
                db.collection("user").document(myuid).update("carname", infomation);
            }

            @Override
            public void onError(Exception taskException) {

            }
        });
    }
    */

    public void load_user_Parking_place_list(String uid, String car,
                                             final OnUserParkinglistFilledListener listener){
        db.collection("users")
                .document(uid)
                .collection(car)
                .orderBy("시간", Query.Direction.DESCENDING)//시간순으로 정렬
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){//리스트 끝날때까지
                            int i = 0;
                            ArrayList<userParkingInfo> UserParkingArray = new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                userParkingInfo temp = new userParkingInfo();
                                temp.memo = document.getString("메모");
                                temp.place = document.getGeoPoint("위치");
                                temp.time = document.getDate("시간");
                                temp.parkingid = document.getId();
                                document.getData();//전부
                                UserParkingArray.add(i, temp);
                                i++;
                            }
                            listener.onUserParkinglistFilled(UserParkingArray);
                        }else{
                            listener.onError(task.getException());
                        }
                    }
                });
    }
    public class carInfo{
        public String carname;
        public String carnum;
        public String fuel;
        public String memo;
        public int number;

    }
}

class userParkingInfo {
    public Date time;
    public GeoPoint place;
    public String memo;
    public String parkingid;
    public boolean allow;
}
class parking_Place{
    public String name;
    public GeoPoint place;
    public Object price;
    public Object rest;
}
class user_Info{
    public String name;
    public String email;
    public String uid;
}
interface OnParkingFilledListener {
    void onParkingFilled(ArrayList<parking_Place> infomation);
    void onError(Exception taskException);
}
interface OnUserParkingFilledListener {
    void onUserInfoFilled(userParkingInfo infomation);
    void onError(Exception taskException);
}
interface OnUserParkinglistFilledListener {
    void onUserParkinglistFilled(ArrayList<userParkingInfo> infomation);
    void onError(Exception taskException);
}
interface OnCarlistFilledListener {
    void onCarlistFilled(ArrayList<ParkingFirebase.carInfo> infomation);
    void onError(Exception taskException);
}
interface OnBooleanListener {
    void onBooleanFilled(Boolean infomation);
    void onError(Exception taskException);
}


package com.example.juchawhich;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class infopushpull extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "infopushpull";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btn1;
    Button btn2;
    Button btn3;
    Button car_btn;
    AlertDialog carDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infopushpull);
        btn1 = findViewById(R.id.mapInfo);
        btn2 = findViewById(R.id.makeFriend);
        btn3 = findViewById(R.id.update_icon);
        car_btn = findViewById(R.id.car_dialog);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        car_btn.setOnClickListener(this);
    }

    public void addParkingMap(String car) {
        // [START add_alan_turing]
        // Create a new user with a first, middle, and last name
        Map<String, Object> user = new HashMap<>();
        user.put("지도", null);
        user.put("serch", true);
        user.put("시간", new Timestamp(new Date()));

        // Add a new document with a generated ID
        db.collection("users")
                .document("앱마다 이름")
                .collection(car)
                .add(user)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        // [END add_alan_turing]
    }

    public void addFriend(String friend) {
        Map<String,Object> user = new HashMap<>();
        user.put(friend,null);

        db.collection("users")
                .document("1")
                .collection("친구")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void getAllUsers(String documentId) {
        // [START get_all_users]
        DocumentReference docRef = db.collection("users")
                .document("앱마다 이름")
                .collection("그랜져")
                .document(documentId);
        Log.d(TAG, "0");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "1");
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "2");
                    if (document.exists()) {
                        Map<String, Object> map = document.getData();
                        for(Map.Entry<String , Object> entry : map.entrySet()){
                            Log.d(TAG, entry.getValue().toString());
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(infopushpull.this);
                            alertDialog.setTitle("차").setMessage(entry.toString());
                            alertDialog.setPositiveButton("OK", dialogListener);
                            alertDialog.setNegativeButton("NO", null);
                            carDialog = alertDialog.create();
                            carDialog.show();

                        }
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        // [END get_all_users]
    }

    public void getMultipleDocs() {
        // [START get_multiple]
        CollectionReference parkingRef = db.collection("users")
                .document("앱마다 이름")
                .collection("그랜져");
        parkingRef.orderBy("시간", Query.Direction.DESCENDING);
        db.collection("users")
                .document("앱마다 이름")
                .collection("그랜져")
                .whereEqualTo("serch", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Date currentDate = document.getDate("시간");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                                String date = dateFormat.format(currentDate);
                                showToast(date);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        // [END get_multiple]
    }


    @Override
    public void onClick(View v) {
        if (v == btn1) {
            addParkingMap("그랜져");
        }
        else if (v == btn2) {
            addFriend("이재현");
        }
        else if (v == car_btn) {
            getMultipleDocs();
        }
    }
    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialog == carDialog && which == DialogInterface.BUTTON_POSITIVE) {
                showToast("car dialog 확인 click....");
            }
        }
    };
    private void showToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
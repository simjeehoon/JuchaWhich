package com.example.juchawhich;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Blocking_list extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    private Context mContext;
    private ArrayList<String> array_mountain;
    private ListView listView;
    private mAdapter mAdapter;
    private FirebaseAuth user = FirebaseAuth.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocking_list);
        setToolbar();

        this.mContext = getApplicationContext();
        listView = (ListView)findViewById(R.id.blocking_list);

        array_mountain = new ArrayList<>();
        mAdapter = new mAdapter(mContext,array_mountain);
        String myuid = user.getUid();
        listView.setAdapter(mAdapter);
        newfriend_Func temp1 = new newfriend_Func();
        temp1.load_Block_Friend_list(myuid,new OnInfomationFilledListener() {
            @Override
            public void onInfomationFilled(ArrayList<friend_Infomation> infomation) {
                for(int i = 0; i < infomation.size(); i++) {
                    array_mountain.add(infomation.get(i).name+"\n"+infomation.get(i).email);

                }
            }

            @Override
            public void onError(Exception taskException) {

            }
        });
    }

    private void setToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //좌측 버튼 설정
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목을 없애줍니다.
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3, menu) ;

        return true ;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.block_friend:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Toast.makeText(getApplicationContext(), "블랙추가버튼 클릭됨", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
        }
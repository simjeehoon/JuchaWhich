package com.example.juchawhich;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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

public class friend_list extends AppCompatActivity {
    private Context mContext;
    private ArrayList<String> array_mountain;
    private ListView listView;
    private mAdapter mAdapter;
    private FirebaseAuth user = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Toolbar toolbar;
    ActionBar actionBar;

    private void setToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //좌측 버튼 설정
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목을 없애줍니다.
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu) ;

        return true ;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend__list);
        setToolbar();
        this.mContext = getApplicationContext();
        listView = (ListView)findViewById(R.id.friend_list1);
        array_mountain = new ArrayList<>();
        mAdapter = new mAdapter(mContext,array_mountain);
        String myuid = user.getUid();

        newfriend_Func temp1 = new newfriend_Func();
        temp1.load_Friend_list1(myuid, new OnInfomationFilledListener() {
            @Override
            public void onInfomationFilled(ArrayList<friend_Infomation> infomation) {
                if(infomation != null) {
                    listView.setAdapter(mAdapter);
                    for (int i = 0; i < infomation.size(); i++) {
                        array_mountain.add(infomation.get(i).name + "\n" + infomation.get(i).email);

                    }
                }
                else {
                }
            }

            @Override
            public void onError(Exception taskException) {

            }

        });




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(),Blocking_list.class);
                startActivity(intent);
                break;
            case R.id.add_friend:
                final newfriend_Func temp = new newfriend_Func();
                Toast.makeText(getApplicationContext(), "친구추가버튼 클릭됨", Toast.LENGTH_SHORT).show();
                final EditText editText = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("친구추가");
                builder.setMessage("이름");
                builder.setView(editText);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        temp.load_Friend_by_name(editText.getText().toString(), new OnFriendFilledListener() {
                            @Override
                            public void onfriendFilled(friend_Infomation infomation) {
                                if(infomation.name != null) {
                                    temp.add_Friend_by_info(user.getUid(), infomation.uid, infomation.name, infomation.email);
                                    Toast.makeText(getApplicationContext(), "친구추가됬습니다.", Toast.LENGTH_SHORT).show();
                                    array_mountain.add(infomation.name + "\n" + infomation.email);
                                    listView.setAdapter(mAdapter);
                                }else{
                                    Toast.makeText(getApplicationContext(), "없는 이름입니다.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Exception taskException) {
                            }

                        });

                    }
                });
                builder.setNegativeButton("NO", null);
                builder.create();
                builder.show();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
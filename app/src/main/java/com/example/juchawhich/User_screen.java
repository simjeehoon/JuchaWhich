package com.example.juchawhich;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class User_screen extends AppCompatActivity {
    TextView textView1;
    TextView textView2;
    Button bt1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);

        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();

        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        textView1 = findViewById(R.id.User1);
        textView2 = findViewById(R.id.User2);
        bt1 = findViewById(R.id.logout_bt);

        if(name != null)
            textView1.setText(name);
        if(email != null)
            textView2.setText(email);


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),"로그아웃되었습니다.",Toast.LENGTH_SHORT).show();
                getIntent().putExtra("LOGOUT",true);
                ParkingMapActivity.shutdown = true;
                Intent intent = new Intent(getApplicationContext(), Login_screen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}


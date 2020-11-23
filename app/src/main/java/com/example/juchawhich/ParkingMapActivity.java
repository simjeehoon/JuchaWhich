package com.example.juchawhich;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class ParkingMapActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_map);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        //actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.parking_screen_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
           case R.id.auto_parking_menu:
               Toast.makeText(getApplicationContext(), "자동주차", Toast.LENGTH_LONG).show();
                break;
            case R.id.share_car_menu:
                Toast.makeText(getApplicationContext(), "차량공유", Toast.LENGTH_LONG).show();
                break;
            case R.id.parking_record_menu:
                Toast.makeText(getApplicationContext(), "주차기록", Toast.LENGTH_LONG).show();
                break;
            case R.id.car_info_menu:
                Toast.makeText(getApplicationContext(), "차량정보", Toast.LENGTH_LONG).show();
                break;
            case R.id.add_car:
                Toast.makeText(getApplicationContext(), "차량 추가", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
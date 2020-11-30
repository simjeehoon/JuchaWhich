package com.example.juchawhich;

import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

public class ParkingMapToolbar {
    private ParkingMapActivity mainActivity;

    private Toolbar toolbar;
    private ActionBar actionBar;

    public ParkingMapToolbar(ParkingMapActivity appCompatActivity){
        mainActivity = appCompatActivity;

        toolbar = mainActivity.findViewById(R.id.toolbar);
        mainActivity.setSupportActionBar(toolbar);
        actionBar = mainActivity.getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true); //좌측 버튼 설정
        actionBar.setHomeAsUpIndicator(R.drawable.menu_icon); //좌측 버튼 아이콘 설정
    }

    public void onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.auto_parking_menu:
                Toast.makeText(mainActivity.getApplicationContext(), "자동주차", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share_car_menu:
                Toast.makeText(mainActivity.getApplicationContext(), "차량공유", Toast.LENGTH_SHORT).show();
                break;
            case R.id.parking_record_menu:
                Toast.makeText(mainActivity.getApplicationContext(), "주차기록", Toast.LENGTH_SHORT).show();
                break;
            case R.id.car_info_menu:
                Toast.makeText(mainActivity.getApplicationContext(), "차량정보", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_car:
                Toast.makeText(mainActivity.getApplicationContext(), "차량 추가", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                if(!mainActivity.getParkingMapSlideMenu().isMenuOpen()) {
                    mainActivity.getParkingMapSlideMenu().openRightMenu();
                }
                break;
        }
    }
}

package com.example.juchawhich;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ParkingMapToolbar {
    private ParkingMapActivity mainActivity;

    private Toolbar toolbar;
    private ActionBar actionBar;
    private Spinner carList;

    private Button addCarButton;
    private boolean carExist = false;
    private int selectedCarIdx;

    public ParkingMapToolbar(ParkingMapActivity appCompatActivity){
        mainActivity = appCompatActivity;

        toolbar = mainActivity.findViewById(R.id.toolbar);
        mainActivity.setSupportActionBar(toolbar);
        actionBar = mainActivity.getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true); //좌측 버튼 설정
        actionBar.setHomeAsUpIndicator(R.drawable.menu_icon); //좌측 버튼 아이콘 설정

        carList = mainActivity.findViewById(R.id.car_list);
        addCarButton = mainActivity.findViewById(R.id.add_car_button_when_no_car);
    }

    public void renewCarList(){
        if(carExist){ //차있으면
            addCarButton.setVisibility(View.GONE);
            carList.setVisibility(View.VISIBLE);
            ArrayList<String> items = new ArrayList<String>();
            items.add("더미");
            items.add("차량 추가...");
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(mainActivity,android.R.layout.simple_list_item_1, items);
            carList.setAdapter(itemsAdapter);
            carList.setSelection(selectedCarIdx);
            final int itemsCnt = items.size();
            carList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position == itemsCnt-1){
                        startAddCarActivity();
                    } else {
                        selectedCarIdx=position;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else { //차없으면
            addCarButton.setVisibility(View.VISIBLE);
            carList.setVisibility(View.GONE);
            addCarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAddCarActivity();
                }
            });
        }
        //차량 불러오기
        //차량 있으면 스피너 없으면 버튼
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
            case android.R.id.home:
                if(!mainActivity.getParkingMapSlideMenu().isMenuOpen()) {
                    mainActivity.getParkingMapSlideMenu().openRightMenu();
                }
                break;
        }
    }

    private void startAddCarActivity(){
        Intent intent = new Intent();
        intent.setClass(mainActivity, AddCarActivity.class);
        mainActivity.startActivityForResult(intent, 100);
    }
}

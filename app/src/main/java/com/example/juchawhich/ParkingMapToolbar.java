package com.example.juchawhich;

import android.content.Intent;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Iterator;

import static com.example.juchawhich.ParkingMapMessageBox.NO_PARKED;
import static com.example.juchawhich.ParkingMapMessageBox.PARKED;

public class ParkingMapToolbar {
    private ParkingMapActivity mainActivity;

    private Toolbar toolbar;
    private ActionBar actionBar;
    private Spinner carList;

    private Button addCarButton;
    private int selectedCarIdx=-1;

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

    ArrayList<String> items;

    public String getCarName(){
        if(selectedCarIdx == -1){
            return null;
        }
        return items.get(selectedCarIdx);
    }
    public void renewCarList(){
        Log.d("h1", "renewOn");
        items = new ArrayList<String>();
        ParkingFirebase.getInstance().load_car_list(FirebaseAuth.getInstance().getUid(), new OnCarlistFilledListener() {
            @Override
            public void onCarlistFilled(ArrayList<ParkingFirebase.carInfo> infomation) {
                Log.d("test10", "d1");
                if (infomation == null) {
                    items = null;
                } else {
                    Iterator<ParkingFirebase.carInfo> iter = infomation.iterator();
                    while (iter.hasNext()) {
                        items.add(iter.next().carname);
                    }
                    Log.d("test10", "d2");
                }
                if (items != null && items.size() != 0) { //차있으면
                    Log.d("h1", "hell");
                    addCarButton.setVisibility(View.GONE);
                    carList.setVisibility(View.VISIBLE);
                    items.add("차량 추가...");
                    ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, items);
                    carList.setAdapter(itemsAdapter);
                    carList.setSelection(selectedCarIdx);
                    final int itemsCnt = items.size();
                    carList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == itemsCnt - 1) {
                                startAddCarActivity();
                            } else {
                                selectedCarIdx = position;
                                ParkingFirebase.getInstance().load_parking_state(FirebaseAuth.getInstance().getUid(), getCarName(), new OnBooleanListener() {
                                    @Override
                                    public void onBooleanFilled(Boolean infomation) {
                                        if (!infomation) {
                                            Log.d("Status11", "no_parked");
                                            mainActivity.getParkingMapMessageBox().statusChange(NO_PARKED);
                                        } else {
                                            Log.d("Status11", "parked");
                                            mainActivity.getParkingMapMessageBox().statusChange(PARKED);
                                        }
                                    }

                                    @Override
                                    public void onError(Exception taskException) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    Log.d("h1", "hell3");
                } else { //차없으면
                    Log.d("test10", "d7");
                    Log.d("h1", "hell52");
                    addCarButton.setVisibility(View.VISIBLE);
                    carList.setVisibility(View.GONE);
                    addCarButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startAddCarActivity();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception taskException) {

            }
        });


        //차량 불러오기
        //차량 있으면 스피너 없으면 버튼
        Log.d("h1", "hell502");
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

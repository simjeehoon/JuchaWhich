package com.example.juchawhich;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ParkingMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    Toolbar toolbar;
    ActionBar actionBar;
    ParkingMapSlideMenu parkingMapSlideMenu;

    View topMsgBox;
    View bottomMsgBox;
    View mapScreen;

    Animation topMsgAppear;
    Animation topMsgDisappear;
    Animation bottomMsgAppear;
    Animation bottomMsgDisappear;

    boolean msgBoxAppear = true;

    private void setActionBar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true);//좌측 버튼 설정
        actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);//좌측 버튼 아이콘 설정
    }


    class BoxTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(msgBoxAppear)
                return true;
            else
                return false;
        }
    }

    private void setAnimationMapClicked(){
        topMsgBox = findViewById(R.id.top_msg_box);
        topMsgBox.setOnTouchListener(new BoxTouchListener());
        bottomMsgBox = findViewById(R.id.bottom_msg_box);
        bottomMsgBox.setOnTouchListener(new BoxTouchListener());
        mapScreen = findViewById(R.id.map_screen);
        mapScreen.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == 1) {
                    if (msgBoxAppear) {
                        topMsgBox.startAnimation(topMsgDisappear);
                        bottomMsgBox.startAnimation(bottomMsgDisappear);
                        topMsgBox.setVisibility(View.GONE);
                        bottomMsgBox.setVisibility(View.GONE);
                        msgBoxAppear = false;
                    } else {
                        topMsgBox.setVisibility(View.VISIBLE);
                        bottomMsgBox.setVisibility(View.VISIBLE);
                        topMsgBox.startAnimation(topMsgAppear);
                        bottomMsgBox.startAnimation(bottomMsgAppear);
                        msgBoxAppear = true;
                    }
                }
                return true;
            }
        });

        topMsgAppear = AnimationUtils.loadAnimation(this,R.anim.top_msgbox_appear);
        topMsgDisappear = AnimationUtils.loadAnimation(this,R.anim.top_msgbox_disappear);
        topMsgDisappear.setFillAfter(true);
        bottomMsgAppear = AnimationUtils.loadAnimation(this,R.anim.bottom_msgbox_appear);
        bottomMsgDisappear = AnimationUtils.loadAnimation(this,R.anim.bottom_msgbox_disappear);
        bottomMsgDisappear.setFillAfter(true);
    }

    private void setMapScreen(){
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        Log.d("googlemapLog", "단계1");
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng Seoul = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(Seoul);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLng(Seoul));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));
        Log.d("googlemapLog", "단계2");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_map);
        parkingMapSlideMenu = new ParkingMapSlideMenu(this);
        setActionBar();
        setAnimationMapClicked();

        setMapScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.parking_screen_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    long initTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if (parkingMapSlideMenu.isMenuOpen()) {
                initTime = 0;
                parkingMapSlideMenu.closeRightMenu();
            } else if(System.currentTimeMillis()-initTime > 3000){
                Toast.makeText(getApplicationContext(), "종료하려면 한번 더 누르세요", Toast.LENGTH_SHORT).show();
                initTime=System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
           case R.id.auto_parking_menu:
                Toast.makeText(getApplicationContext(), "자동주차", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share_car_menu:
                Toast.makeText(getApplicationContext(), "차량공유", Toast.LENGTH_SHORT).show();
                break;
            case R.id.parking_record_menu:
                Toast.makeText(getApplicationContext(), "주차기록", Toast.LENGTH_SHORT).show();
                break;
            case R.id.car_info_menu:
                Toast.makeText(getApplicationContext(), "차량정보", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_car:
                Toast.makeText(getApplicationContext(), "차량 추가", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                if(!parkingMapSlideMenu.isMenuOpen()) {
                    parkingMapSlideMenu.openRightMenu();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
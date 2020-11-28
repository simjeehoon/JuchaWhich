package com.example.juchawhich;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.LinearLayout;
import android.widget.Toast;

public class ParkingMapActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;

    LinearLayout slideMenu;
    View darkBackground;
    Animation menuOpenAnim;
    Animation menuCloseAnim;
    Animation onDarkAnim;
    Animation offDarkAnim;
    boolean isMenuOpen;

    private void setActionBar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true);//좌측 버튼 설정
        actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);//좌측 버튼 아이콘 설정
    }

    private void setSlideMenu(){
        slideMenu = findViewById(R.id.slide_menu);
        darkBackground = findViewById(R.id.dark_background);
        slideMenu.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        darkBackground.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isMenuOpen) {
                    closeRightMenu();
                    isMenuOpen=false;
                }
                return true;
            }
        });
        onDarkAnim = AnimationUtils.loadAnimation(this, R.anim.alpha_on);
        onDarkAnim.setFillAfter(true);
        offDarkAnim = AnimationUtils.loadAnimation(this, R.anim.alpha_off);
        menuOpenAnim = AnimationUtils.loadAnimation(this, R.anim.menu_open_to_right);
        menuOpenAnim.setFillAfter(true);
        menuCloseAnim = AnimationUtils.loadAnimation(this,R.anim.menu_close_to_left);

        Animation.AnimationListener animListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationStart(Animation animation) {

            }
        };
        menuOpenAnim.setAnimationListener(animListener);
        menuCloseAnim.setAnimationListener(animListener);
    }

    private void openRightMenu(){
        darkBackground.setVisibility(View.VISIBLE);
        slideMenu.setVisibility(View.VISIBLE);
        slideMenu.startAnimation(menuOpenAnim);
        darkBackground.startAnimation(onDarkAnim);
    }

    private void closeRightMenu(){
        slideMenu.startAnimation(menuCloseAnim);
        darkBackground.startAnimation(offDarkAnim);
        darkBackground.setVisibility(View.GONE);
        slideMenu.setVisibility(View.GONE);
    }

    public void invitationList(View v){
        Toast.makeText(getApplicationContext(), "초대목록", Toast.LENGTH_SHORT).show();
    }
    public void friends(View v){
        Toast.makeText(getApplicationContext(), "친구들", Toast.LENGTH_SHORT).show();
    }
    public void myAccount(View v){
        Toast.makeText(getApplicationContext(), "내계정", Toast.LENGTH_SHORT).show();
    }
    public void setting(View v){
        Toast.makeText(getApplicationContext(), "설정", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_map);
        setSlideMenu();
        setActionBar();
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
            if (isMenuOpen) {
                initTime = 0;
                isMenuOpen = false;
                closeRightMenu();
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
                if(!isMenuOpen) {
                    isMenuOpen = true;
                    openRightMenu();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
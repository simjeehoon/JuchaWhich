package com.example.juchawhich;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
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
                closeRightMenu();
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
            case android.R.id.home:
                openRightMenu();
                Toast.makeText(getApplicationContext(), "좌측 메뉴", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
package com.example.juchawhich;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ParkingMapSlideMenu {
    Activity mainActivity;

    LinearLayout slideMenu;
    View darkBackground;

    View invitationListView;
    View friendsView;
    View myAccountsView;
    View settingsView;

    Animation menuOpenAnim;
    Animation menuCloseAnim;
    Animation onDarkAnim;
    Animation offDarkAnim;

    boolean menuOpen = false;
    private void setMenuOpen(boolean opened){menuOpen = opened;}
    public boolean isMenuOpen(){return menuOpen;}

    public void openRightMenu() {
        setMenuOpen(true);
        darkBackground.setVisibility(View.VISIBLE);
        slideMenu.setVisibility(View.VISIBLE);
        slideMenu.startAnimation(menuOpenAnim);
        darkBackground.startAnimation(onDarkAnim);
    }
    public void closeRightMenu() {
        setMenuOpen(false);
        slideMenu.startAnimation(menuCloseAnim);
        darkBackground.startAnimation(offDarkAnim);
        darkBackground.setVisibility(View.GONE);
        slideMenu.setVisibility(View.GONE);
    }
    private  void setTouchEventWhenSlide(){
        slideMenu.setOnTouchListener(new ConsumeTouchEvent());
        darkBackground.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isMenuOpen() && event.getAction() == 1) {
                    closeRightMenu();
                }
                return true;
            }
        });
    }
    private void setAnimation(){
        onDarkAnim = AnimationUtils.loadAnimation(mainActivity, R.anim.alpha_on);
        onDarkAnim.setFillAfter(true);
        offDarkAnim = AnimationUtils.loadAnimation(mainActivity, R.anim.alpha_off);
        menuOpenAnim = AnimationUtils.loadAnimation(mainActivity, R.anim.menu_open_to_right);
        menuOpenAnim.setFillAfter(true);
        menuCloseAnim = AnimationUtils.loadAnimation(mainActivity, R.anim.menu_close_to_left);
    }
    private void setMenuOption(){
        invitationListView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(mainActivity.getApplicationContext(), "초대 목록 기능", Toast.LENGTH_SHORT).show();
            }
        });
        friendsView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(mainActivity.getApplicationContext(), "친구목록 기능", Toast.LENGTH_SHORT).show();
            }
        });
        myAccountsView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(mainActivity.getApplicationContext(), "내 계정 기능", Toast.LENGTH_SHORT).show();
            }
        });
        settingsView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(mainActivity.getApplicationContext(), "설정 기능", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ParkingMapSlideMenu(Activity activity) {
        mainActivity = activity;
        slideMenu = mainActivity.findViewById(R.id.slide_menu);
        darkBackground = mainActivity.findViewById(R.id.dark_background);
        setTouchEventWhenSlide();
        setAnimation();
        invitationListView = mainActivity.findViewById(R.id.invitation_list);
        friendsView = mainActivity.findViewById(R.id.friends);
        myAccountsView = mainActivity.findViewById(R.id.my_account);
        settingsView = mainActivity.findViewById(R.id.settings);
        setMenuOption();
    }
}

package com.example.juchawhich;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ParkingMapSlideMenu {
    private Activity mainActivity;

    private LinearLayout slideMenu;
    private View darkBackground;

    private View invitationListView;
    private View friendsView;
    private View myAccountsView;
    private View settingsView;

    private Animation menuOpenAnim;
    private Animation menuCloseAnim;
    private Animation onDarkAnim;
    private Animation offDarkAnim;

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
                Intent intent = new Intent(mainActivity, friend_list.class);
                mainActivity.startActivity(intent);
            }
        });
        myAccountsView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, User_screen.class);
                mainActivity.startActivityForResult(intent,776);
            }
        });
        settingsView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(mainActivity.getApplicationContext(), "설정 기능", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setUserNameAndEmail(){
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        String pname = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String pemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        TextView name = mainActivity.findViewById(R.id.user_name);
        TextView email = mainActivity.findViewById(R.id.user_email);
        name.setText(pname);
        email.setText(pemail);
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
        setUserNameAndEmail();
    }
}

package com.example.juchawhich;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class ParkingMapMessageBox {
    private Activity mainActivity;

    private View topMsgBox;
    private View bottomMsgBox;
    private View mapScreen;

    private Animation topMsgAppear;
    private Animation topMsgDisappear;
    private Animation bottomMsgAppear;
    private Animation bottomMsgDisappear;

    private boolean msgBoxAppear = true;

    class BoxTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(msgBoxAppear)
                return true;
            else
                return false;
        }
    }

    private void loadBoxAndMapView(){
        topMsgBox = mainActivity.findViewById(R.id.top_msg_box);
        bottomMsgBox = mainActivity.findViewById(R.id.bottom_msg_box);
        mapScreen = mainActivity.findViewById(R.id.map_screen);
    }

    private void setBoxTouchListener(){
        topMsgBox.setOnTouchListener(new BoxTouchListener());
        bottomMsgBox.setOnTouchListener(new BoxTouchListener());
    }

    private void loadAnimation(){
        topMsgAppear = AnimationUtils.loadAnimation(mainActivity, R.anim.top_msgbox_appear);
        topMsgDisappear = AnimationUtils.loadAnimation(mainActivity, R.anim.top_msgbox_disappear);
        topMsgDisappear.setFillAfter(true);
        bottomMsgAppear = AnimationUtils.loadAnimation(mainActivity, R.anim.bottom_msgbox_appear);
        bottomMsgDisappear = AnimationUtils.loadAnimation(mainActivity, R.anim.bottom_msgbox_disappear);
        bottomMsgDisappear.setFillAfter(true);
    }

    public void showMessageBox(){
        msgBoxAppear = true;
        topMsgBox.setVisibility(View.VISIBLE);
        bottomMsgBox.setVisibility(View.VISIBLE);
        topMsgBox.startAnimation(topMsgAppear);
        bottomMsgBox.startAnimation(bottomMsgAppear);
    }

    public void hideMessageBox(){
        msgBoxAppear = false;
        topMsgBox.startAnimation(topMsgDisappear);
        bottomMsgBox.startAnimation(bottomMsgDisappear);
        topMsgBox.setVisibility(View.GONE);
        bottomMsgBox.setVisibility(View.GONE);
    }

    public boolean isMsgBoxAppear(){
        return msgBoxAppear;
    }

    public ParkingMapMessageBox(Activity activity){
        mainActivity = activity;
        loadBoxAndMapView();
        setBoxTouchListener();
        loadAnimation();
    }
}

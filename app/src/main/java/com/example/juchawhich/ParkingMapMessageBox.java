package com.example.juchawhich;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class ParkingMapMessageBox {
    private ParkingMapActivity mainActivity;

    private View topMsgBox;
    private View bottomMsgBox;

    private View buttons;
    private Button parkingInLotButton;
    private Button normalParkingButton;
    private View curPositionButton;

    private Animation topMsgAppear;
    private Animation topMsgDisappear;
    private Animation bottomMsgAppear;
    private Animation bottomMsgDisappear;
    private Animation fadein;

    private TextView address;

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

    private void loadViews(){
        topMsgBox = mainActivity.findViewById(R.id.top_msg_box);
        bottomMsgBox = mainActivity.findViewById(R.id.bottom_msg_box);
        buttons = mainActivity.findViewById(R.id.parking_buttons);
        normalParkingButton = mainActivity.findViewById(R.id.normal_parking_button);
        parkingInLotButton = mainActivity.findViewById(R.id.parking_in_lot_button);
        curPositionButton = mainActivity.findViewById(R.id.current_position_button);
        address=mainActivity.findViewById(R.id.point_address);
    }

    private void setBoxTouchListener(){
        topMsgBox.setOnTouchListener(new BoxTouchListener());
        bottomMsgBox.setOnTouchListener(new BoxTouchListener());
        buttons.setOnTouchListener(new BoxTouchListener());
        parkingInLotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "hello");
            }
        });
        normalParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "hello");
            }
        });
        curPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getGoogleMapController().moveToCurPosition();
                address.setText("현재 위치: " + mainActivity.getGoogleMapController().getCurrentPositionAddress());
            }
        });
    }

    private void loadAnimation(){
        topMsgAppear = AnimationUtils.loadAnimation(mainActivity, R.anim.top_msgbox_appear);
        topMsgDisappear = AnimationUtils.loadAnimation(mainActivity, R.anim.top_msgbox_disappear);
        topMsgDisappear.setFillAfter(true);
        bottomMsgAppear = AnimationUtils.loadAnimation(mainActivity, R.anim.bottom_msgbox_appear);
        bottomMsgDisappear = AnimationUtils.loadAnimation(mainActivity, R.anim.bottom_msgbox_disappear);
        bottomMsgDisappear.setFillAfter(true);
        fadein = AnimationUtils.loadAnimation(mainActivity,R.anim.alpha_on);
    }

    public void showMessageBox(){
        msgBoxAppear = true;
        topMsgBox.setVisibility(View.VISIBLE);
        bottomMsgBox.setVisibility(View.VISIBLE);
        buttons.setVisibility(View.VISIBLE);
        parkingInLotButton.setVisibility(View.VISIBLE);
        normalParkingButton.setVisibility(View.VISIBLE);
        topMsgBox.startAnimation(topMsgAppear);
        bottomMsgBox.startAnimation(bottomMsgAppear);
        buttons.startAnimation(fadein);
    }

    public void hideMessageBox(){
        msgBoxAppear = false;
        topMsgBox.startAnimation(topMsgDisappear);
        bottomMsgBox.startAnimation(bottomMsgDisappear);
        topMsgBox.setVisibility(View.INVISIBLE);
        bottomMsgBox.setVisibility(View.GONE);
        buttons.setVisibility(View.GONE);
        parkingInLotButton.setVisibility(View.GONE);
        normalParkingButton.setVisibility(View.GONE);
    }

    public boolean isMsgBoxAppear(){
        return msgBoxAppear;
    }

    public ParkingMapMessageBox(ParkingMapActivity activity){
        mainActivity = activity;
        loadViews();
        setBoxTouchListener();
        loadAnimation();
    }
}

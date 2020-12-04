package com.example.juchawhich;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

public class ParkingMapMessageBox {
    private ParkingMapActivity mainActivity;

    private View topMsgBox;
    private View topParkingDataBox;
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

    private TextView addressText;

    private boolean msgBoxAppear = true;
    private int status;

    static final int NO_CAR_DATA = 0;
    static final int NO_PARKED = 1;
    static final int PARKED = 2;

    class BoxTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(msgBoxAppear)
                return true;
            else
                return false;
        }
    }

    public void statusChange(int s){
        if(s == PARKED){
            topMsgBox.setVisibility(View.GONE);
            topParkingDataBox.setVisibility(View.VISIBLE);
            topMsgBox = topParkingDataBox;
        } else if(s == NO_CAR_DATA){
            topParkingDataBox.setVisibility(View.GONE);
            topMsgBox.setVisibility(View.VISIBLE);
        }
    }

    private void loadViews(){
        topMsgBox = mainActivity.findViewById(R.id.top_msg_box);
        bottomMsgBox = mainActivity.findViewById(R.id.bottom_msg_box);
        buttons = mainActivity.findViewById(R.id.parking_buttons);
        normalParkingButton = mainActivity.findViewById(R.id.normal_parking_button);
        parkingInLotButton = mainActivity.findViewById(R.id.parking_in_lot_button);
        curPositionButton = mainActivity.findViewById(R.id.current_position_button);
        addressText=mainActivity.findViewById(R.id.point_address);

        topParkingDataBox = mainActivity.findViewById(R.id.parked_data_top_box);
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
                Intent intent = new Intent();
                intent.setClass(mainActivity, NormalParkingActivity.class);

                mainActivity.startActivityForResult(intent, 110);
                statusChange(PARKED);
            }
        });
        curPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getGoogleMapController().moveToCurPosition();
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
        bottomMsgDisappear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                parkingInLotButton.setVisibility(View.GONE);
                normalParkingButton.setVisibility(View.GONE);
                buttons.setVisibility(View.GONE);
                bottomMsgBox.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        fadein = AnimationUtils.loadAnimation(mainActivity,R.anim.alpha_on);
    }

    private void showTopBox(){
        topMsgBox.setVisibility(View.VISIBLE);
        topMsgBox.startAnimation(topMsgAppear);
    }

    private void showBottomBox(){
        bottomMsgBox.setVisibility(View.VISIBLE);
        bottomMsgBox.startAnimation(bottomMsgAppear);
    }

    public void showMessageBox(){
        msgBoxAppear = true;
        showTopBox();
        showBottomBox();
        buttons.setVisibility(View.VISIBLE);
        parkingInLotButton.setVisibility(View.VISIBLE);
        normalParkingButton.setVisibility(View.VISIBLE);

        buttons.startAnimation(fadein);
    }

    private void hideTopBox(){
        topMsgBox.startAnimation(topMsgDisappear);
        topMsgBox.setVisibility(View.INVISIBLE);
    }

    private void hideBottomBox(){
        bottomMsgBox.startAnimation(bottomMsgDisappear);
    }
    public void hideMessageBox(){
        msgBoxAppear = false;
        hideTopBox();
        hideBottomBox();

        parkingInLotButton.setVisibility(View.INVISIBLE);
        normalParkingButton.setVisibility(View.INVISIBLE);
        buttons.setVisibility(View.INVISIBLE);
    }

    public boolean isMsgBoxAppear(){
        return msgBoxAppear;
    }

    public ParkingMapMessageBox(ParkingMapActivity activity){
        mainActivity = activity;
        loadViews();
        setBoxTouchListener();
        loadAnimation();
       // statusChange(PARKED);
    }

    public void setCurAddress(){
        addressText.setText("현재 위치: " + mainActivity.getGoogleMapController().getCurrentPositionAddress());
    }
}

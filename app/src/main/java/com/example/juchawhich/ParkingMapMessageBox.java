package com.example.juchawhich;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class ParkingMapMessageBox {
    private ParkingMapActivity mainActivity;

    private View topMsgBox;
    private View topParkingDataBox;
    private View bottomMsgBox;

    private View buttons;
    private Button parkingInLotButton;
    private Button normalParkingButton;
    private View curPositionButton;

    View hejeBtn;
    private Animation topMsgAppear;
    private Animation topMsgDisappear;
    private Animation bottomMsgAppear;
    private Animation bottomMsgDisappear;

    private Animation fadein;

    private TextView addressText;

    private boolean msgBoxAppear = true;
    private int status = NO_CAR_DATA;

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

    void setParkDataBox(){
        TextView t = mainActivity.findViewById(R.id.when_paked);
    }

    public void statusChange(int s){
        status = s;
        hideMessageBox();

        topMsgBox.setVisibility(View.GONE);
        bottomMsgBox.setVisibility(View.GONE);
        if(s == PARKED){
            setParkDataBox();
            topMsgBox = topParkingDataBox;
            bottomMsgBox = mainActivity.findViewById(R.id.button_bar_when_parking);

            topMsgBox.setVisibility(View.GONE);
            bottomMsgBox.setVisibility(View.GONE);

            buttons.setVisibility(View.GONE);
        } else if(s == NO_CAR_DATA){
            topMsgBox = mainActivity.findViewById(R.id.no_car_box);
            bottomMsgBox = mainActivity.findViewById(R.id.bottom_msg_box);

            topMsgBox.setVisibility(View.GONE);
            bottomMsgBox.setVisibility(View.GONE);

            buttons.setVisibility(View.GONE);
        } else if(s == NO_PARKED){
            topMsgBox = mainActivity.findViewById(R.id.no_parked_view);
            bottomMsgBox = mainActivity.findViewById(R.id.bottom_msg_box);

            topMsgBox.setVisibility(View.GONE);
            bottomMsgBox.setVisibility(View.GONE);
        }
    }

    private void loadViews(){
        topParkingDataBox = mainActivity.findViewById(R.id.parked_data_top_box);
        buttons = mainActivity.findViewById(R.id.parking_buttons);

        normalParkingButton = mainActivity.findViewById(R.id.normal_parking_button);
        parkingInLotButton = mainActivity.findViewById(R.id.parking_in_lot_button);

        curPositionButton = mainActivity.findViewById(R.id.current_position_button);

        addressText=mainActivity.findViewById(R.id.point_address);

        topMsgBox = mainActivity.findViewById(R.id.no_parked_view);
        bottomMsgBox =  mainActivity.findViewById(R.id.no_parked_view);
        hejeBtn = mainActivity.findViewById(R.id.parking_set_button);
        hejeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParkingFirebase.getInstance().update_user_car_no_parked(FirebaseAuth.getInstance().getUid(), mainActivity.getParkingMapToolbar().getCarName());
                statusChange(NO_PARKED);
            }
        });
    }

    private void setBoxTouchListener(){
        topMsgBox.setOnTouchListener(new BoxTouchListener());
        bottomMsgBox.setOnTouchListener(new BoxTouchListener());
        buttons.setOnTouchListener(new BoxTouchListener());
        parkingInLotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        normalParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mainActivity, NormalParkingActivity.class);
                intent.putExtra("CAR_NAME", mainActivity.getParkingMapToolbar().getCarName());
                mainActivity.startActivityForResult(intent, 110);
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
        //topMsgBox.startAnimation(topMsgAppear);
    }

    private void showBottomBox(){
        bottomMsgBox.setVisibility(View.VISIBLE);
        //bottomMsgBox.startAnimation(bottomMsgAppear);
    }

    private void showParkingButton(){
        buttons.setVisibility(View.VISIBLE);
        parkingInLotButton.setVisibility(View.VISIBLE);
        normalParkingButton.setVisibility(View.VISIBLE);
        //buttons.startAnimation(fadein);
    }

    public void showMessageBox(){
        msgBoxAppear = true;
        showTopBox();
        showBottomBox();
        if(status == NO_PARKED){
            showParkingButton();
        }
    }

    private void hideTopBox(){
       // topMsgBox.startAnimation(topMsgDisappear);
        topMsgBox.setVisibility(View.INVISIBLE);
    }

    private void hideBottomBox(){
      //  bottomMsgBox.startAnimation(bottomMsgDisappear);
        bottomMsgBox.setVisibility(View.INVISIBLE);
    }

    private void hideParkingButton(){
        parkingInLotButton.setVisibility(View.INVISIBLE);
        normalParkingButton.setVisibility(View.INVISIBLE);
        buttons.setVisibility(View.INVISIBLE);
    }

    public void hideMessageBox(){
        msgBoxAppear = false;
        hideTopBox();
        hideBottomBox();
        if(status == NO_PARKED){
            hideParkingButton();
        }
    }

    public boolean isMsgBoxAppear(){
        return msgBoxAppear;
    }

    public ParkingMapMessageBox(ParkingMapActivity activity){
        mainActivity = activity;
        loadViews();
        setBoxTouchListener();
        loadAnimation();
        statusChange(NO_CAR_DATA);
        showMessageBox();
    }

    public void setCurAddress(){

        addressText.setText("현재 위치: " + mainActivity.getGoogleMapController().getCurrentPositionAddress());
    }
}

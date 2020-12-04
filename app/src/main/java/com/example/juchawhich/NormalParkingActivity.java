package com.example.juchawhich;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class NormalParkingActivity extends AppCompatActivity implements OnMapReadyCallback {
    Location toParkingLocation;

    View cancelButton;
    View textMemoButton;
    View cameraMemoButton;
    View parkingButton;
    View curPositionButton;

    GoogleMap map;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        LatLng latlng = new LatLng(23.33,123.123);
        map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        map.animateCamera(CameraUpdateFactory.zoomTo(12));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_parking);
        if(CurrentLocationManager.isLocationAvailable())
            toParkingLocation=CurrentLocationManager.getLastLocation();
        else
            Log.d("debugMsg", "nonAvailable");
        setButtons();
    }

    private void moveToLocation(Location location){
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    private void setButtons(){
        cancelButton = findViewById(R.id.cancel_button);
        textMemoButton = findViewById(R.id.memo_button);
        cameraMemoButton = findViewById(R.id.camera_button);
        parkingButton = findViewById(R.id.parking_set_button);
        curPositionButton = findViewById(R.id.current_position_button_on_parking);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textMemoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("buttons","memo1");
            }
        });
        cameraMemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("buttons","memo2");
            }
        });
        parkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        curPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentLocationManager.checkPermission(NormalParkingActivity.this))
                    if(CurrentLocationManager.isLocationAvailable())
                        toParkingLocation = CurrentLocationManager.getLastLocation();
                else
                    CurrentLocationManager.requestPermission(NormalParkingActivity.this);
                if(toParkingLocation != null){
                    moveToLocation(toParkingLocation);
                }
            }
        });

    }
}
package com.example.juchawhich;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NormalParkingActivity extends AppCompatActivity implements OnMapReadyCallback {
    Location toParkingLocation;

    View cancelButton;
    View textMemoButton;
    View cameraMemoButton;
    View parkingButton;
    View curPositionButton;

    GoogleMap map;
    private Marker curPositionMarker;

    TextView addressTextView;

    private void setGoogleMap() {
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.google_map_2);
        mapFragment.getMapAsync(this);
    }

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_on_normal_parking_activity);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //좌측 버튼 설정
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목을 없애줍니다.
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap m) {
        map = m;
        map.getUiSettings().setMapToolbarEnabled(false);
        MarkerOptions markerOptions = new MarkerOptions();

        if(CurrentLocationManager.isLocationAvailable()) {
            toParkingLocation = CurrentLocationManager.getLastLocation();
            markerOptions.position(new LatLng(toParkingLocation.getLatitude(), toParkingLocation.getLongitude()));
            moveToLocation(toParkingLocation);
            curPositionMarker = map.addMarker(markerOptions);
            addressTextView.setText("주소: "+CurrentLocationManager.getAddress(this, toParkingLocation));
        }else{
            Location loc = new Location("selectedPosition");
            loc.setLatitude(37);
            loc.setLongitude(137);
            moveToLocation(loc);
        }
        m.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                curPositionMarker.remove();
                MarkerOptions markerOptions1 = new MarkerOptions();
                markerOptions1.position(latLng);
                curPositionMarker = map.addMarker(markerOptions1);

                toParkingLocation.setLatitude(latLng.latitude);
                toParkingLocation.setLongitude(latLng.longitude);
                moveToLocation(toParkingLocation);
                addressTextView.setText("주소: "+CurrentLocationManager.getAddress(NormalParkingActivity.this, toParkingLocation));
            }
        });
        Log.d("onMapReady", "called");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debugMsg", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_parking);

        setToolbar();

        if(CurrentLocationManager.isLocationAvailable())
            toParkingLocation=CurrentLocationManager.getLastLocation();
        else
            Log.d("debugMsg", "nonAvailable");
        setButtons();
        Log.d("debugMsg", "onCreate");

        setGoogleMap();
        addressTextView = findViewById(R.id.address_to_parking);
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
                    if(CurrentLocationManager.isLocationAvailable()) {
                        toParkingLocation = CurrentLocationManager.getLastLocation();
                        curPositionMarker.remove();
                        MarkerOptions markerOptions1 = new MarkerOptions();
                        markerOptions1.position(new LatLng(toParkingLocation.getLatitude(),toParkingLocation.getLongitude()));
                        curPositionMarker = map.addMarker(markerOptions1);
                        moveToLocation(toParkingLocation);
                        addressTextView.setText("주소: "+CurrentLocationManager.getAddress(NormalParkingActivity.this, toParkingLocation));
                    }
                else
                    CurrentLocationManager.requestPermission(NormalParkingActivity.this);
                if(toParkingLocation != null){
                    moveToLocation(toParkingLocation);
                }
            }
        });

    }
}
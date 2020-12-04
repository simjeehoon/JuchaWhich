package com.example.juchawhich;

import android.app.FragmentManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GoogleMapController implements OnMapReadyCallback {

    private ParkingMapActivity mainActivity;
    private CurrentLocationManager currentLocationManager;
    private GoogleMap map;
    private boolean autoMoveToCurPositionSucceeded;
    private Marker curPositionMarker;

    public CurrentLocationManager getCurrentLocationManager(){
        return currentLocationManager;
    }

    private void setCurrentPositionMarker(LatLng pos){
         if(curPositionMarker != null)
             curPositionMarker.remove();
         MarkerOptions curMarkerOptions = new MarkerOptions();
         curMarkerOptions.position(pos);
         curMarkerOptions.title("현재 위치");
         curMarkerOptions.snippet("현재위치입니다");
         curPositionMarker = map.addMarker(curMarkerOptions);
    }

    @Override
    public void onMapReady(GoogleMap m) {
        map = m;
        map.getUiSettings().setMapToolbarEnabled(false);

        LatLng latlng = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");

        curPositionMarker = map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        map.animateCamera(CameraUpdateFactory.zoomTo(16));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(mainActivity.getParkingMapMessageBox().isMsgBoxAppear()) {
                    mainActivity.getParkingMapMessageBox().hideMessageBox();
                }
                else{
                    mainActivity.getParkingMapMessageBox().showMessageBox();
                }
            }
        });
    }

    public GoogleMapController(ParkingMapActivity activity){
        mainActivity = activity;
        FragmentManager fragmentManager = mainActivity.getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
        currentLocationManager = new CurrentLocationManager(mainActivity, this);

    }

    public boolean moveToCurPosition(){
        if(!currentLocationManager.checkPermission(mainActivity))
           currentLocationManager.requestPermission(mainActivity);
        if(currentLocationManager.checkPermission(mainActivity) && currentLocationManager.isLocationAvailable()){
            Location curLocation = currentLocationManager.getLastLocation();
            LatLng latlng = new LatLng(curLocation.getLatitude(), curLocation.getLongitude());
            setCurrentPositionMarker(latlng);
            map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            map.animateCamera(CameraUpdateFactory.zoomTo(15));
            return true;
        } else {
            Toast.makeText(mainActivity.getApplicationContext(), "권한 획득 실패", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    void moveToCurpositionOnCreate(){
        if(!autoMoveToCurPositionSucceeded) {
            moveToCurPosition();
            autoMoveToCurPositionSucceeded=true;
        }
    }

    public String getAddress(Location location){
        return CurrentLocationManager.getAddress(mainActivity, location);
    }

    public String getCurrentPositionAddress(){
        Location location;
        if(currentLocationManager.isLocationAvailable())
            location = currentLocationManager.getLastLocation();
        else
            return null;
        return getAddress(location);
    }
}

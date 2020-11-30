package com.example.juchawhich;

import android.app.FragmentManager;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapController implements OnMapReadyCallback {

    private ParkingMapActivity mainActivity;
    private CurrentLocationManager currentLocationManager;
    private GoogleMap map;
    private boolean autoMoveToCurPositionSucceeded;

    public CurrentLocationManager getCurrentLocationManager(){
        return currentLocationManager;
    }

    @Override
    public void onMapReady(GoogleMap m) {
        map = m;

        LatLng latlng = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");

        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        map.animateCamera(CameraUpdateFactory.zoomTo(16));

        //map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(mainActivity.getParkingMapMessageBox().isMsgBoxAppear()) {
                    mainActivity.getParkingMapMessageBox().hideMessageBox();
                    if(currentLocationManager.isLocationAvailable()){
                    }
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
        if(!currentLocationManager.checkPermission())
           currentLocationManager.requestPermission();
        if(currentLocationManager.checkPermission() && currentLocationManager.isLocationAvailable()){
            Location curLocation = currentLocationManager.getLastLocation();
            LatLng latlng = new LatLng(curLocation.getLatitude(), curLocation.getLongitude());
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
}

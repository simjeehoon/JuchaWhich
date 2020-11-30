package com.example.juchawhich;

import android.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapController implements OnMapReadyCallback {
    ParkingMapActivity mainActivity;
    CurrentLocationManager currentLocationManager;

    public CurrentLocationManager getCurrentLocationManager(){
        return currentLocationManager;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng Seoul = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(Seoul);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLng(Seoul));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

    public GoogleMapController(ParkingMapActivity activity){
        mainActivity = activity;
        FragmentManager fragmentManager = mainActivity.getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
        currentLocationManager = new CurrentLocationManager(mainActivity);
    }


}

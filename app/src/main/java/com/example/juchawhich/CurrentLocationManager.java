package com.example.juchawhich;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

public class CurrentLocationManager {
    private AppCompatActivity activity;
    private GoogleMapController googleMapController;

    boolean fineLocationPermission;
    boolean coarseLocationPermission;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    Location lastLocation;

    public boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            fineLocationPermission = true;
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            coarseLocationPermission = true;
        return fineLocationPermission && coarseLocationPermission;
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 105 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                fineLocationPermission = true;
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED)
                coarseLocationPermission = true;
        }
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 105);
    }

    public CurrentLocationManager(AppCompatActivity appCompatActivity, GoogleMapController googleMapController) {
        activity = appCompatActivity;
        this.googleMapController = googleMapController;
        requestLocationUpdates();
    }

    public boolean requestLocationUpdates(){
        if(!checkPermission()){
            requestPermission();
        }
        if(checkPermission()) {
            fusedLocationProviderClient = new FusedLocationProviderClient(activity);
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setFastestInterval(500);
            locationRequest.setInterval(1000);

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    lastLocation = locationResult.getLastLocation();
                    googleMapController.moveToCurpositionOnCreate();
                }
            }, activity.getMainLooper());
            return true;
        } else {

            return false;
        }
    }

    public boolean isLocationAvailable(){
        return lastLocation != null;
    }

    public Location getLastLocation(){
        return lastLocation;
    }
}

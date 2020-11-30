package com.example.juchawhich;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class CurrentLocationManager {
    AppCompatActivity activity;
    boolean fineLocationPermission;
    boolean coarseLocationPermission;

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

    public CurrentLocationManager(AppCompatActivity appCompatActivity) {
        activity = appCompatActivity;
        if (!checkPermission())
            requestPermission();
    }
}

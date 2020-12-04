package com.example.juchawhich;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.io.IOException;
import java.util.List;

public class CurrentLocationManager {
    private GoogleMapController googleMapController;
    private ParkingMapActivity parkingMapActivity;

    static boolean fineLocationPermission;
    static boolean coarseLocationPermission;

    static private FusedLocationProviderClient fusedLocationProviderClient;
    static private LocationRequest locationRequest;

    static private Location lastLocation;

    static public boolean checkPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            fineLocationPermission = true;
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            coarseLocationPermission = true;
        return fineLocationPermission && coarseLocationPermission;
    }

    static public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 105 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                fineLocationPermission = true;
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED)
                coarseLocationPermission = true;
        }
    }

    static public void requestPermission(AppCompatActivity activity){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 105);
    }

    public CurrentLocationManager(ParkingMapActivity appCompatActivity, GoogleMapController googleMapController) {
        this.googleMapController = googleMapController;
        parkingMapActivity = appCompatActivity;
        if(!checkPermission(appCompatActivity))
            requestPermission(appCompatActivity);
        requestLocationUpdates(appCompatActivity);
    }

    public boolean requestLocationUpdates(AppCompatActivity activity){
        if(checkPermission(activity)) {
            fusedLocationProviderClient = new FusedLocationProviderClient(activity);
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setFastestInterval(500);
            locationRequest.setInterval(1000);

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                int i;
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    lastLocation = locationResult.getLastLocation();
                    googleMapController.moveToCurpositionOnCreate();
                    parkingMapActivity.getParkingMapMessageBox().setCurAddress();
                    Log.d("locationUpdate", "location : "+lastLocation.getLatitude()+", "+lastLocation.getLongitude()+",    "+i++);
                }
            }, activity.getMainLooper());
            return true;
        } else {

            return false;
        }
    }

    static public boolean isLocationAvailable(){
        return lastLocation != null;
    }

    static public Location getLastLocation(){
        return lastLocation;
    }

    static String getAddress(AppCompatActivity activity, Location location){
        Geocoder geocoder = new Geocoder(activity);
        String address;
        try{
            List<Address> resultList = geocoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(), 1
            );
            address = resultList.get(0).getAddressLine(0);
        } catch (IOException e) {
            address = new String("주소 얻기 실패");
        }
        return address;
    }
}

package com.example.juchawhich;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ParkingMapActivity extends AppCompatActivity {

    private ParkingMapSlideMenu parkingMapSlideMenu;
    private ParkingMapMessageBox parkingMapMessageBox;
    private ParkingMapToolbar parkingMapToolbar;
    private GoogleMapController googleMapController;

    public ParkingMapSlideMenu getParkingMapSlideMenu(){
        return parkingMapSlideMenu;
    }

    public ParkingMapMessageBox getParkingMapMessageBox(){
        return parkingMapMessageBox;
    }

    public GoogleMapController getGoogleMapController() {return googleMapController; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_map);
        parkingMapToolbar = new ParkingMapToolbar(this);
        parkingMapSlideMenu = new ParkingMapSlideMenu(this);
        parkingMapMessageBox = new ParkingMapMessageBox(this);
        googleMapController = new GoogleMapController(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.parking_screen_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    long initTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if (parkingMapSlideMenu.isMenuOpen()) {
                initTime = 0;
                parkingMapSlideMenu.closeRightMenu();
            } else if(System.currentTimeMillis()-initTime > 3000){
                Toast.makeText(getApplicationContext(), "종료하려면 한번 더 누르세요", Toast.LENGTH_SHORT).show();
                initTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        parkingMapToolbar.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        googleMapController.getCurrentLocationManager().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

}
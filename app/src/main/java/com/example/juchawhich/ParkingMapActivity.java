package com.example.juchawhich;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ParkingMapActivity extends AppCompatActivity {

    ParkingMapSlideMenu parkingMapSlideMenu;
    ParkingMapMessageBox parkingMapMessageBox;
    ParkingMapToolbar parkingMapToolbar;
    GoogleMapController googleMapController;

    public ParkingMapSlideMenu getParkingMapSlideMenu(){
        return parkingMapSlideMenu;
    }

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
}
package com.example.juchawhich;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.firebase.auth.FirebaseAuth;

public class AddCarActivity extends AppCompatActivity {

    EditText carNameBox;
    EditText carNumberBox;
    Spinner fuelTypeSpinner;
    EditText carMemoBox;
    String fuelTypeString;
    Button addButton;

    Toolbar toolbar;
    ActionBar actionBar;

    //매니패스트에서 해당 액티비티의 액션바는 제거해야합니다


    private void setToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        setToolbar();
        carNameBox=findViewById(R.id.car_name_box);
        carNumberBox=findViewById(R.id.car_number_box);

        carMemoBox=findViewById(R.id.car_memo_box);
        addButton=findViewById(R.id.button_add_car);
        addButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             CarInfo carInfo = getCarInfo();
                                             if(carInfo != null){
                                                 Log.d("car_data", "이름:"+carInfo.carName);
                                                 Log.d("car_data", "번호:"+carInfo.carNumber);
                                                 Log.d("car_data", "연료:"+carInfo.fuelType);
                                                 Log.d("car_data", "메모:"+carInfo.carMemo);
                                                 ParkingFirebase.getInstance().add_car(FirebaseAuth.getInstance().getUid(), carInfo.carName, carInfo.carNumber, carInfo.fuelType, carInfo.carMemo);
                                                 finish();
                                             }
                                         }
                                     });

                fuelTypeSpinner = findViewById(R.id.fuel_type);
        fuelTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0)
                    fuelTypeString=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public CarInfo getCarInfo(){
        String carNameString = carNameBox.getText().toString();
        if(carNameString.trim().getBytes().length <= 0){
            Toast.makeText(getApplicationContext(), "차량 별명은 필수 항목입니다.", Toast.LENGTH_LONG).show();
            return null;
        }
        CarInfo infoToReturn = new CarInfo(carNameBox.getText().toString());
        String carNumberString = carNumberBox.getText().toString();
        if(carNumberString.trim().getBytes().length > 0){
            infoToReturn.carNumber=carNumberString;
        }
        String carMemoString = carMemoBox.getText().toString();
        if(carMemoString.trim().getBytes().length > 0){
            infoToReturn.carMemo=carMemoString;
        }
        if(fuelTypeString != null){
            infoToReturn.fuelType=fuelTypeString;
        } else {

        }
        return infoToReturn;
    }
}
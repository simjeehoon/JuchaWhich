package com.example.juchawhich;


import android.os.Parcel;
import android.os.Parcelable;

public class CarInfo implements Parcelable {
    public String carName;
    public String carNumber;
    public String fuelType;
    public String carMemo;
    public CarInfo(String carName){
        this.carName=carName;
    }

    protected CarInfo(Parcel in) {
        carName = in.readString();
        carNumber = in.readString();
        fuelType = in.readString();
        carMemo = in.readString();
    }

    public static final Creator<CarInfo> CREATOR = new Creator<CarInfo>() {
        @Override
        public CarInfo createFromParcel(Parcel in) {
            return new CarInfo(in);
        }

        @Override
        public CarInfo[] newArray(int size) {
            return new CarInfo[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(carName);
        dest.writeString(carNumber);
        dest.writeString(fuelType);
        dest.writeString(carMemo);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
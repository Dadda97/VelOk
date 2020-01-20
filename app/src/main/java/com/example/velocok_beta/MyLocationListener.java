package com.example.velocok_beta;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;


public class MyLocationListener implements LocationListener  {

    private static final String TAG = "MyLocationListener ";

    private MySpeedList speedList;
    AtomicBoolean isUpdated;
    AtomicBoolean isMonitoring;

    Location[] locations;



    MyLocationListener(MySpeedList list,AtomicBoolean atomBoolUpd,AtomicBoolean atomBoolMon){
        speedList=list;
        isUpdated=atomBoolUpd;
        isMonitoring=atomBoolMon;
    }

    @Override
    public void onLocationChanged(Location location) {
        Location start= new Location("");
        start.setLongitude(8.908138);
        start.setLatitude(44.411093);
        Log.d(TAG,Float.toString(start.distanceTo(location)));
        if(!isMonitoring.get()){
            if(start.distanceTo(location)<20){
                Log.d(TAG,"start monitoring");
                isMonitoring.set(true);
            }
        }
        if(isMonitoring.get()) {
            speedList.add(location.getSpeed() * 3.6F);
            isUpdated.set(true);
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}

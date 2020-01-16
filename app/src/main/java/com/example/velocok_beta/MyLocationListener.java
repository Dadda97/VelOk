package com.example.velocok_beta;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;



public class MyLocationListener implements LocationListener  {

    private static final String TAG = "MyLocationListener ";

    private MySpeedList speedList;

    MyLocationListener(MySpeedList list){
        speedList=list;
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d(TAG,Float.toString(location.getSpeed()));
        speedList.add((double) location.getSpeed());
        Log.d(TAG,Float.toString(speedList.getAverageSpeed()));
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

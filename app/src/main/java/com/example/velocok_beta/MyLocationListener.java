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

    MyLocationListener(MySpeedList list,AtomicBoolean atomBool){
        speedList=list;
        isUpdated=atomBool;
    }

    @Override
    public void onLocationChanged(Location location) {

        speedList.add(location.getSpeed());
        isUpdated.set(true);
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

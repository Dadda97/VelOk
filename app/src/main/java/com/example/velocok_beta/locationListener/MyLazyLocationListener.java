package com.example.velocok_beta.locationListener;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.example.velocok_beta.MainActivity;
import com.example.velocok_beta.structure.MyPath;

public class MyLazyLocationListener implements LocationListener {
    private final static String TAG = "MyLazyLocationListener ";

    MyPath[] paths;

    Context welcomeActivity;

    int GPSIntervalSec;

    public MyLazyLocationListener(Context context, int GPSInterval) {

        GPSIntervalSec=GPSInterval/1000;

        welcomeActivity = context;

        paths = new MyPath[2];

        Location[] auxArray = new Location[4];
        Location aux = new Location("");
        aux.setLongitude(8.911541);
        aux.setLatitude(44.413269);
        auxArray[0] = aux;
        Location aux2 = new Location("");
        aux2.setLongitude(8.928097);
        aux2.setLatitude(44.408727);
        auxArray[1] = aux2;
        Location aux3 = new Location("");
        aux3.setLongitude(8.929323);
        aux3.setLatitude(44.404016);
        auxArray[2] = aux3;
        Location aux4 = new Location("");
        aux4.setLongitude(8.939971);
        aux4.setLatitude(44.397107);
        auxArray[3] = aux4;
        paths[0] = new MyPath(auxArray, 60, "SOPRAELEVATA dir. Levante");

        Location[] aux2_Array = new Location[4];
        Location aux2_ = new Location("");
        aux2_.setLongitude(8.940013);
        aux2_.setLatitude(44.397148);
        aux2_Array[0] = aux2_;
        Location aux2_2 = new Location("");
        aux2_2.setLongitude(8.932496);
        aux2_2.setLatitude(44.400323);
        aux2_Array[1] = aux2_2;
        Location aux2_3 = new Location("");
        aux2_3.setLongitude(8.929369);
        aux2_3.setLatitude(44.404043);
        aux2_Array[2] = aux2_3;
        Location aux2_4 = new Location("");
        aux2_4.setLongitude(8.907428);
        aux2_4.setLatitude(44.410919);
        aux2_Array[3] = aux2_4;
        paths[1] = new MyPath(aux2_Array, 60, "SOPRAELEVATA dir. Ponente");
        Log.d(TAG, "listener created");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, location.toString());
        float speed = (location.getSpeed()*3600)/1000;
        double checkPointDistance =(speed/2)*GPSIntervalSec;
        Log.d(TAG, String.format("checkPointDistance: %2f ",checkPointDistance));

        for (MyPath path : paths) {
            Log.d(TAG, path.getName().concat(String.valueOf(path.getStart().distanceTo(location))));
            if (path.getStart().distanceTo(location) < checkPointDistance) {
                Log.d(TAG, "Starting path: ".concat(path.getName()));
                Intent mainActivity = new Intent(welcomeActivity, MainActivity.class);
                mainActivity.putExtra("path", path);
                welcomeActivity.startActivity(mainActivity);
                break;
            }
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

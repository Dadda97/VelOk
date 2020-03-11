package com.example.velocok_beta;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

public class MyLazyLocationListener implements LocationListener {
    private  final static String TAG = "MyLazyLocationListener ";

    MyPath[] paths;

    Context welcomeActivity;
    MyLazyLocationListener(Context context){

        welcomeActivity=context;

        paths= new MyPath[2];
        Location[]auxArray = new Location[3];
        Location aux= new Location("");
        aux.setLongitude(8.911541);
        aux.setLatitude(44.413269);
        auxArray[0]=aux;
        Location aux2= new Location("");
        aux2.setLongitude(8.928097);
        aux2.setLatitude(44.408727);
        auxArray[1]=aux2;
        Location aux3= new Location("");
        aux3.setLongitude(8.929323);
        aux3.setLatitude(44.404016);
        auxArray[2]=aux3;
        paths[1]= new MyPath(auxArray, 60, "SOPRAELEVATA dir. Levante");

        auxArray = new Location[3];
        aux= new Location("");
        aux.setLongitude(8.911541);
        aux.setLatitude(44.413269);
        auxArray[0]=aux;
        aux2= new Location("");
        aux2.setLongitude(9.14257);
        aux2.setLatitude(44.414366);
        auxArray[1]=aux2;
        aux3= new Location("");
        aux3.setLongitude(8.918802);
        aux3.setLatitude(44.415063);
        auxArray[2]=aux3;
        paths[0]= new MyPath(auxArray, 60, "TEST");
        Log.d(TAG, "listener created");

        Log.d(TAG, "LazyLogic Launched");
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, location.toString());

        for(MyPath path:paths) {
            Log.d(TAG, String.valueOf(path.getStart().distanceTo(location)));
            Log.d(TAG, path.getStart().toString());
            if (path.getStart().distanceTo(location) < 100) {
                Log.d(TAG, "Starting path: ".concat(path.getName()));
                Intent mainActivity = new Intent(welcomeActivity,MainActivity.class);
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

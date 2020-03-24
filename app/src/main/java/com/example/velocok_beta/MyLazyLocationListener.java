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

        Location[] aux2_Array = new Location[4];
        Location aux2_= new Location("");
        aux2_.setLongitude(8.911541);
        aux2_.setLatitude(44.413269);
        aux2_Array[0]=aux2_;
        Location aux2_2= new Location("");
        aux2_2.setLongitude(8.914390);
        aux2_2.setLatitude(44.414390);
        aux2_Array[1]=aux2_2;
        Location aux2_3= new Location("");
        aux2_3.setLongitude(8.919950);
        aux2_3.setLatitude(44.415287);
        aux2_Array[2]=aux2_3;
        Location aux2_4= new Location("");
        aux2_4.setLongitude(8.926216);
        aux2_4.setLatitude(44.413105);
        aux2_Array[3]=aux2_4;
        paths[0]= new MyPath(aux2_Array, 60, "TEST");
        Log.d(TAG, "listener created");
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, location.toString());

        for(MyPath path:paths) {
            Log.d(TAG, path.getName().concat(String.valueOf(path.getStart().distanceTo(location))));

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

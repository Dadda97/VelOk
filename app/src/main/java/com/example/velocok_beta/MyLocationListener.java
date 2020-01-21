package com.example.velocok_beta;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;


public class MyLocationListener implements LocationListener  {

    private static final String TAG = "MyLocationListener ";

    private MySpeedList speedList;
    AtomicBoolean isUpdated;
    AtomicBoolean isMonitoring;

    MyPath andata;
    Location end = null;


    MyLocationListener(MySpeedList list,AtomicBoolean atomBoolUpd,AtomicBoolean atomBoolMon){
        speedList=list;
        isUpdated=atomBoolUpd;
        isMonitoring=atomBoolMon;

        Location[]auxArray = new Location[3];
        Location aux= new Location("");
        aux.setLongitude(8.908138);
        aux.setLatitude(44.411093);
        auxArray[0]=aux;
        Location aux2= new Location("");
        aux2.setLongitude(8.911964);
        aux2.setLatitude(44.413459);
        auxArray[1]=aux2;
        Location aux3= new Location("");
        aux3.setLongitude(8.928115);
        aux3.setLatitude(44.408726);
        auxArray[2]=aux3;

        andata= new MyPath(auxArray, 60);

    }

    @Override
    public void onLocationChanged(Location location) {

        if(!isMonitoring.get()){
            if(andata.getStart().distanceTo(location)<20){
                Log.d(TAG,"start monitoring");
                isMonitoring.set(true);
                end = andata.getNext();
                if(end==null){
                    Log.d(TAG,"FINITO");
                    isMonitoring.set(false);
                }
            }
        }
        if(isMonitoring.get()) {
            Log.d(TAG,String.valueOf(end.distanceTo(location)));
            if(end.distanceTo(location)<20){
                andata.setAvgSpeed(speedList.getAverageSpeed());
                isMonitoring.set(false);
            }
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

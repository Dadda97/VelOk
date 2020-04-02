package com.example.velocok_beta;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;


public class MyLocationListener extends AppCompatActivity implements LocationListener   {

    private static final String TAG = "MyLocationListener ";

    private MySpeedList speedList;
    AtomicBoolean isUpdated;
    AtomicBoolean isMonitoring;
    MyPath path;
    TextView avgSpeed;
    TextView instSpeed;
    DecimalFormat df = new DecimalFormat("#.##");

    MyLocationListener(MySpeedList list, AtomicBoolean atomBoolUpd, AtomicBoolean atomBoolMon, MyPath startedPath, Context mainContext){
        speedList=list;
        isUpdated=atomBoolUpd;
        isMonitoring=atomBoolMon;
        path= startedPath;
        Activity mainActivity= (Activity) mainContext;
        mainActivity.setContentView(R.layout.activity_main);
        avgSpeed= mainActivity.findViewById(R.id.avgSpeedView);
        instSpeed= mainActivity.findViewById(R.id.instantSpeedView);
    }

    @Override
    public void onLocationChanged(Location location) {

        if(!isMonitoring.get()){

            if (path.getStart().distanceTo(location) < 20) {
                Log.d(TAG,  "start monitoring");
                isMonitoring.set(true);
            }
        }

        if(isMonitoring.get()) {
            if(path.getEnd().distanceTo(location)<20){
                isMonitoring.set(false);
                path.setAvgSpeedOfCheckpoint(speedList.getAverageSpeed());
                speedList.clear();
                Log.d(TAG,"finito");
                for (float sp:path.getAvgArray()){
                    Log.d(TAG,String.valueOf(sp));
                };
                return;
            }
            Log.d(TAG,String.valueOf(path.getNextCheckpoint().distanceTo(location)));
            if(path.getNextCheckpoint().distanceTo(location)<20){

                path.setAvgSpeedOfCheckpoint(speedList.getAverageSpeed());
                speedList.clear();
                Log.d(TAG,"check");
            }
            float speed = location.getSpeed();
            speedList.add(speed );
            instSpeed.setText(df.format(speed));
            avgSpeed.setText(df.format(speedList.getAverageSpeed()));
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

package com.example.velocok_beta;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;


public class MyLocationListener extends AppCompatActivity implements LocationListener   {

    private static final String TAG = "MyLocationListener ";

    private MySpeedList speedList;
    AtomicBoolean isUpdated;
    AtomicBoolean isMonitoring;
    MyPath path;
    TextView avgSpeed;
    TextView instSpeed;
    TextView sectorsView;
    DecimalFormat df = new DecimalFormat("#.00");
    Activity mainActivity;
    Context mainContext;
    int countSector=1;

    MyLocationListener(MySpeedList list, AtomicBoolean atomBoolUpd, AtomicBoolean atomBoolMon, MyPath startedPath, Context mainContext_){
        speedList=list;
        isUpdated=atomBoolUpd;
        isMonitoring=atomBoolMon;
        path= startedPath;
        mainContext= mainContext_;
        mainActivity= (Activity) mainContext_;
        mainActivity.setContentView(R.layout.activity_main);
        avgSpeed= mainActivity.findViewById(R.id.avgSpeedView);
        instSpeed= mainActivity.findViewById(R.id.instantSpeedView);
        sectorsView =mainActivity.findViewById(R.id.sectorsView);
    }

    @Override
    public void onLocationChanged(Location location) {

        if(!isMonitoring.get()){

            if (path.getStart().distanceTo(location) < 30) {
                Log.d(TAG,  "start monitoring");
                isMonitoring.set(true);
            }
        }

        if(isMonitoring.get()) {
            if(path.getEnd().distanceTo(location)<30){
                isMonitoring.set(false);
                path.setAvgSpeedOfCheckpoint(speedList.getAverageSpeed());

                Log.d(TAG,"FINISHED");
                for (float sp:path.getAvgArray()){
                    Log.d(TAG,String.valueOf(sp));
                };
                finishMonitoring();


                return;
            }
            Log.d(TAG, String.valueOf(path.getNextCheckpoint()));
            Log.d(TAG,String.valueOf(path.getNextCheckpoint().distanceTo(location)));
            if(path.getNextCheckpoint().distanceTo(location)<30){
                Log.d(TAG,"CHECKPOINT");
                path.setAvgSpeedOfCheckpoint(speedList.getAverageSpeed());

                sectorsView.setText(path.getSectorsMessage());

                speedList.clear();
            }
            float speed = location.getSpeed();
            speedList.add(speed );
            instSpeed.setText(df.format(speed));
            avgSpeed.setText(df.format(speedList.getAverageSpeed()));
            isUpdated.set(true);
        }
    }

    private void finishMonitoring(){
        Log.d(TAG, "finishing");
        AlertDialog.Builder ad = new AlertDialog.Builder(mainActivity);

        ad.setTitle(Html.fromHtml("<font color='#FF7F27'>This is a test</font>"));
        ad.setMessage(path.getSectorsMessage());
        ad.create();
        MyDatabase DB = new MyDatabase(mainContext);
        DB.insert(path);
        DB.getAll();
        final AlertDialog closingDialog= ad.create();
        closingDialog.show();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                closingDialog.dismiss();
                speedList.clear();
                timer.cancel();
                Intent welcomeActivity = new Intent(mainContext,WelcomeActivity.class);
                mainContext.startActivity(welcomeActivity);
                finish();
            }
        }, 5000);

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

package com.example.velocok_beta.locationListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.velocok_beta.structure.MyPath;
import com.example.velocok_beta.structure.MySpeedList;
import com.example.velocok_beta.R;
import com.example.velocok_beta.WelcomeActivity;
import com.example.velocok_beta.database.MyDatabase;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyLocationListener extends AppCompatActivity implements LocationListener {

    private static final String TAG = "MyLocationListener ";

    private MySpeedList speedList;
    Boolean isUpdated = false;
    Boolean isMonitoring = false;
    MyPath path;
    TextView avgSpeed;
    TextView instSpeed;
    TextView sectorsView;
    DecimalFormat df = new DecimalFormat("#.00");
    Activity mainActivity;
    Context mainContext;
    Thread speed_notification;
    ExecutorService executor;
    SharedPreferences mPref;
    int THRESHOLD;
    boolean isOverSpeed_notification_enabled;
    boolean overspeed_notification_loop;
    int GPSIntervalSec;
    //  Location prev = new Location("");        //FOR EMULATOR GPS PROBLEM ANALYSIS


    public MyLocationListener(MyPath startedPath, Context mainContext_, int GPSInterval) {
        speedList = new MySpeedList();
        //  prev.setLongitude(8.928097);        //FOR EMULATOR GPS PROBLEM ANALYSIS
        //  prev.setLatitude(44.408727);        //FOR EMULATOR GPS PROBLEM ANALYSIS
        path = startedPath;
        mainContext = mainContext_;
        mainActivity = (Activity) mainContext_;
        mainActivity.setContentView(R.layout.activity_main);
        avgSpeed = mainActivity.findViewById(R.id.avgSpeedView);
        instSpeed = mainActivity.findViewById(R.id.instantSpeedView);
        sectorsView = mainActivity.findViewById(R.id.sectorsView);
        mPref = PreferenceManager.getDefaultSharedPreferences(mainContext_);
        GPSIntervalSec = GPSInterval/1000;
        THRESHOLD = Integer.parseInt(mPref.getString("threshold_sound", "3"));
        isOverSpeed_notification_enabled = mPref.getBoolean("speed_notification", true);
        overspeed_notification_loop = false;
        executor = Executors.newSingleThreadExecutor();
        speed_notification = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(mainContext, notification);
                for (int alarm = 0; alarm < THRESHOLD; alarm++) {
                    r.play();
                    while (r.isPlaying()) {
                    }
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        float speed = (location.getSpeed()*3600)/1000;
        Log.d(TAG,"location change listener");
        Log.d(TAG,String.format("Curr Speed M/S %2f K/M %2f",location.getSpeed(),speed));
        Log.d(TAG,location.toString());
        double checkPointDistance =(speed/2)*GPSIntervalSec;
        //  Log.d(TAG, String.format("velocita da coord: %2f, velocita da getSpeed: %2f ",(prev.distanceTo(location)*3600)/1000,speed));        //FOR EMULATOR GPS PROBLEM ANALYSIS
        //  prev= location;                                                                                                                     //FOR EMULATOR GPS PROBLEM ANALYSIS
       //   sectorsView.setText(String.format("check: %2f  \n Distanza: %2f\n Velocità: %2f KM/H",checkPointDistance,path.getStart().distanceTo(location),speed));     //FOR PHYSICAL DEVICE DEBUG PURPOSE

        Log.d(TAG,String.format("checkPointDistance %2f",checkPointDistance));
        if (!isMonitoring) {
            if (path.getStart().distanceTo(location) < checkPointDistance) {
                Log.d(TAG, "start monitoring");
                isMonitoring = true;
            }
        }

        if (isMonitoring) {
            if (path.getEnd().distanceTo(location) < checkPointDistance) {
                isMonitoring = false;
                path.setAvgSpeedOfCheckpoint(speedList.getAverageSpeed());

                finishMonitoring();
                return;
            }
            if (path.getNextCheckpoint().distanceTo(location) < checkPointDistance) {
                path.setAvgSpeedOfCheckpoint(speedList.getAverageSpeed());

                sectorsView.setText(path.getSectorsMessage());

                speedList.clear();
            }
            speedList.add(speed);
            instSpeed.setText(df.format(speed));
            Float avgspd = speedList.getAverageSpeed();
            avgSpeed.setText(df.format(avgspd));
            if (avgspd > path.getSpeedLimit()) {
                avgSpeed.setTextColor(ContextCompat.getColor(mainContext, R.color.colorAccent));
                if (!overspeed_notification_loop && isOverSpeed_notification_enabled) {
                    overspeed_notification_loop = true;
                    executor.submit(speed_notification);
                }
            } else {
                avgSpeed.setTextAppearance(mainContext, R.style.speedtext);
                overspeed_notification_loop = false;
            }
            isUpdated = true;
        }
    }

    private void finishMonitoring() {
        AlertDialog.Builder ad = new AlertDialog.Builder(mainActivity);
        String title = mainContext.getResources().getString(R.string.resume_dialog_title);
        ad.setTitle(Html.fromHtml("<font color='#FF7F27'>".concat(title).concat("</font>")));
        ad.setMessage(path.getSectorsMessage());
        ad.create();
        MyDatabase DB = new MyDatabase(mainContext);
        DB.insert(path);
        DB.getAll();
        final AlertDialog closingDialog = ad.create();
        closingDialog.show();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                closingDialog.dismiss();
                speedList.clear();
                timer.cancel();
                Intent welcomeActivity = new Intent(mainContext, WelcomeActivity.class);
                mainContext.startActivity(welcomeActivity);
                finish();
            }
        }, 12000);

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

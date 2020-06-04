package com.example.velocok_beta;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    AtomicBoolean isUpdated;
    MySpeedList speedList;
    AtomicBoolean isMonitoring;

    TextView avgSpeedView;
    TextView instantSpeedView;
    Button monitoringButton;

    SharedPreferences mPreferences;
    int mDayLight;


    boolean overSpeed_notification_loop = false;
    boolean isOverSpeed_notification_enabled;
    int THRESHOLD;
    Thread speed_notification;

    private void alarmFunction() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        for (int alarm = 0; alarm < THRESHOLD; alarm++) {
            r.play();
            while(r.isPlaying()){}
        }

    }

    TextWatcher myTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Double.parseDouble(s.toString()) > 60) {
                    avgSpeedView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    if (!overSpeed_notification_loop && isOverSpeed_notification_enabled) {
                        overSpeed_notification_loop = true;
                        speed_notification.start();
                    }
                } else {
                    //avgSpeedView.setTextColor(getResources().getColor(R.color.black));
                    avgSpeedView.setBackgroundColor(0);
                    // TODO if sound is playing stop it
                    overSpeed_notification_loop = false;
                   // if (r.isPlaying()) { r.stop(); }
                }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    LocationManager locationManager;
    LocationListener locationListener;

    MyLogicTask myLogicTask;

    int gpsInterval = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mDayLight = Integer.parseInt(mPreferences.getString("darkmode_preference", "0"));
        isOverSpeed_notification_enabled = mPreferences.getBoolean("speed_notification", true);
        THRESHOLD = Integer.parseInt(mPreferences.getString("threshold_sound", "3"));
        AppCompatDelegate.setDefaultNightMode(mDayLight);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        avgSpeedView = findViewById(R.id.avgSpeedView);
        instantSpeedView = findViewById(R.id.instantSpeedView);

        speed_notification =  new Thread(new Runnable() {
            @Override
            public void run() {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                for (int alarm = 0; alarm < THRESHOLD; alarm++) {
                    r.play();
                    while(r.isPlaying()){}
                }
            }
        });

        avgSpeedView.addTextChangedListener(myTextWatcher);

        speedList = new MySpeedList();
        isUpdated = new AtomicBoolean(false);     //maybe better in MySpeedList
        isMonitoring = new AtomicBoolean(false);

        MyPath startedPath = (MyPath) getIntent().getParcelableExtra("path");
        Log.d(TAG, "STARTED path: ".concat(startedPath.getName()));
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener(speedList, isUpdated, isMonitoring, startedPath, this );




        checkPermission();

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, gpsInterval, 0, locationListener);

        myLogicTask = new MyLogicTask(speedList, isUpdated, avgSpeedView, instantSpeedView);

        myLogicTask.execute();



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    @SuppressLint("MissingPermission")


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //TODO add execute of mycore

                } else {
                    Toast toast = Toast.makeText(this, "The app cannot work without GPS permission", Toast.LENGTH_LONG);

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission( this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG,"no permissions");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1
            );

            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}

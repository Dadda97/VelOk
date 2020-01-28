package com.example.velocok_beta;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    Switch theme;

    SharedPreferences mPreferences;
    SharedPreferences.Editor preferencesEditor;
    int mDayLight;

    TextWatcher myTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.getClass().equals(Double.class) && Double.parseDouble(s.toString()) > 60) {
                //avgSpeedView.setTextColor(getResources().getColor(R.color.colorAccent));
                avgSpeedView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            } else {
                //avgSpeedView.setTextColor(getResources().getColor(R.color.black));
                avgSpeedView.setBackgroundColor(0);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    CompoundButton.OnCheckedChangeListener mySwitchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mDayLight == AppCompatDelegate.MODE_NIGHT_NO) {
                mDayLight = AppCompatDelegate.MODE_NIGHT_YES;
                preferencesEditor.putInt("theme", AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                mDayLight = AppCompatDelegate.MODE_NIGHT_NO;
                preferencesEditor.putInt("theme", AppCompatDelegate.MODE_NIGHT_NO);
            }
            //AppCompatDelegate.setDefaultNightMode(mDayLight);
            //getDelegate().applyDayNight();
            preferencesEditor.apply();
            recreate();

        }
    };

    LocationManager locationManager;
    LocationListener locationListener;

    MyLogicTask myLogicTask;

    int gpsInterval = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPreferences = getSharedPreferences("com.example.velocok", MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        mDayLight = mPreferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(mDayLight);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        avgSpeedView = findViewById(R.id.avgSpeedView);
        instantSpeedView = findViewById(R.id.instantSpeedView);


        theme = findViewById(R.id.lightAndDark);
        if (mDayLight == AppCompatDelegate.MODE_NIGHT_YES) {
            theme.setChecked(true);
        }
        theme.setOnCheckedChangeListener(mySwitchListener);


        speedList = new MySpeedList();
        isUpdated = new AtomicBoolean(false);     //maybe better in MySpeedList
        isMonitoring = new AtomicBoolean(false);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener(speedList, isUpdated, isMonitoring);


        avgSpeedView.addTextChangedListener(myTextWatcher);

        checkPermission();

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, gpsInterval, 0, locationListener);

        myLogicTask = new MyLogicTask(speedList, isUpdated, avgSpeedView, instantSpeedView);

        myLogicTask.execute();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "no permissions");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

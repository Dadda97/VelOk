package com.example.velocok_beta;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.velocok_beta.locationListener.MyLocationListener;
import com.example.velocok_beta.structure.MyPath;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    TextView avgSpeedView;
    TextView instantSpeedView;

    SharedPreferences mPreferences;
    int mDayLight;

    LocationManager locationManager;
    LocationListener locationListener;

    int gpsInterval = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mDayLight = Integer.parseInt(mPreferences.getString("darkmode_preference", "0"));
        AppCompatDelegate.setDefaultNightMode(mDayLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        avgSpeedView = findViewById(R.id.avgSpeedView);
        instantSpeedView = findViewById(R.id.instantSpeedView);

        checkPermission();

        MyPath startedPath = getIntent().getParcelableExtra("path");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener(startedPath, this);
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @SuppressWarnings("NullableProblems")
    @SuppressLint("MissingPermission")

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length == 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast toast = Toast.makeText(this, "The app cannot work without GPS permission", Toast.LENGTH_LONG);
                toast.show();
            }else{
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, gpsInterval, 0, locationListener);
            }
        } else {
            throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1
            );
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

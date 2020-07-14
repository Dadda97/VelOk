package com.example.velocok_beta;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.example.velocok_beta.locationListener.MyLazyLocationListener;
import com.example.velocok_beta.provider.MyNewsProvider;
import com.example.velocok_beta.provider.MyWeatherProvider;


public class WelcomeActivity extends AppCompatActivity {
    private final String TAG = "Welcome Activity";

    MyNewsProvider newsProvider;
    MyWeatherProvider weatherProvider;

    LocationManager locationManager;
    LocationListener locationListener;
    int gpsInterval = 5000;

    SharedPreferences mPreferences;
    int mDayLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mDayLight = Integer.parseInt(mPreferences.getString("darkmode_preference", "0"));
        AppCompatDelegate.setDefaultNightMode(mDayLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initNews();

        initWeather();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLazyLocationListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public void onStart() {
        super.onStart();

        checkPermission();
        checkGPS();

    }

    @Override
    public void onResume() { //to be removed?
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
        Log.d(TAG, "LazyLocation REMOVED");
    }

    @SuppressLint("MissingPermission")
    public void checkGPS() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder ad_builder = new AlertDialog.Builder(this);
            AlertDialog ad;
            ad_builder.setTitle(R.string.GPS_required_title_ad);
            ad_builder.setMessage(R.string.GPS_required_text_ad);
            ad_builder.setPositiveButton(R.string.GPS_required_positive_ad, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent gpsOptionsIntent = new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsOptionsIntent);

                }
            });
            ad_builder.setNegativeButton(R.string.GPS_required_negative_ad, null);
            ad = ad_builder.create();
            ad.show();

        }
        if(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, gpsInterval, 0, locationListener);
            Log.d(TAG, "LazyLocation REGISTERED");
        }
    }

    public void initNews() {
        Log.d(TAG, "STARTING Init News....");
        LinearLayout newsParent = findViewById(R.id.newsParent);

        newsProvider = new MyNewsProvider(newsParent);
        newsProvider.execute();
    }

    public void openNewsBody(View v) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        int newsId = Integer.parseInt((String) v.getTag());
        ad.setTitle(newsProvider.getNewsTitle(newsId));
        ad.setMessage(newsProvider.getNewsBody(newsId));
        ad.show();
    }

    public void initWeather() {
        Log.d(TAG, "STARTING Init Weather....");
        LinearLayout weatherParent = findViewById(R.id.weatherParent);

        weatherProvider = new MyWeatherProvider(weatherParent);
        weatherProvider.execute();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast toast = Toast.makeText(this, "The app cannot work without GPS permission", Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    Log.d(TAG,"Registering locationListener aa");
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, gpsInterval, 0, locationListener);
                }
                break;
            }
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast toast = Toast.makeText(this, "Without Internet permission the information shown in the app will be reduced", Toast.LENGTH_LONG);
                    toast.show();
                    toast.cancel();
                }
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void checkPermission() {
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.e(TAG, "no GPS permissions");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1
            );
        }
        if (!hasPermission(Manifest.permission.INTERNET) && !hasPermission(Manifest.permission.ACCESS_NETWORK_STATE)) {
            Log.e(TAG, "no INTERNET permissions");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, 2
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
        Intent i;
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.history_activity:
                i = new Intent(this, History.class);
                startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }
}

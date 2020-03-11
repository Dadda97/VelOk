package com.example.velocok_beta;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class WelcomeActivity extends AppCompatActivity {
    private final String TAG="Welcome Activity";
    //////////////////FOR Weather & News//////////////////
    private LinearLayout newsParent;
    private int numberOfNews;
    MyNewsProvider newsProvider;
    private LinearLayout weatherParent;
    private int numberOfForecast;
    MyWeatherProvider weatherProvider;
    //////////////////FOR GPS & location//////////////////
    LocationManager locationManager;
    LocationListener locationListener;
    int gpsInterval = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        checkPermission();

        initNews();

        initWeather();


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLazyLocationListener(this);


    }

    @Override
    public void onResume(){
        super.onResume();
        checkPermission();

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, gpsInterval, 0, locationListener);


    }

    @Override
    public void onPause(){
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    public void initNews(){
        Log.d(TAG,"STARTING Init News....");
        newsParent = findViewById(R.id.newsParent);
        numberOfNews =  newsParent.getChildCount();     //number of news defined by XML

        newsProvider = new MyNewsProvider(numberOfNews);

        Picasso.get().setLoggingEnabled(true);

        for (int i = 0; i< numberOfNews;i++){

            LinearLayout news= (LinearLayout) newsParent.getChildAt(i);

            TextView text= (TextView) news.getChildAt(0);
            text.setText(newsProvider.getNewsTitle(i));

            ImageView image= (ImageView) news.getChildAt(1);
            image.setContentDescription(newsProvider.getNewsTitle(i));
            Picasso.get().load(newsProvider.getNewsImagesURL(i)).resize(500, 500)
                    .centerCrop().into(image);
        }
        Log.d(TAG,"FINISHED Init News....");

    }

    public void openNewsBody(View v){
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        int newsId = Integer.parseInt((String)v.getTag());
        ad.setTitle(newsProvider.getNewsTitle(newsId));
        ad.setMessage(newsProvider.getNewsBody(newsId));
        ad.show();
    }
    public void initWeather(){
        Log.d(TAG,"STARTING Init Weather....");
        weatherParent = findViewById(R.id.weatherParent);
        numberOfForecast =  weatherParent.getChildCount();     //number of weather defined by XML

        weatherProvider = new MyWeatherProvider(numberOfForecast);
        for (int i = 0; i< numberOfForecast;i++){

            LinearLayout forecast= (LinearLayout) weatherParent.getChildAt(i);

            TextView hour = (TextView) forecast.getChildAt(0);
            hour.setText(weatherProvider.getWeatherTime(i));

            ImageView image= (ImageView) forecast.getChildAt(1);
            image.setContentDescription(weatherProvider.getWeatherIconURL(i));
            Picasso.get().load(weatherProvider.getWeatherIconURL(i)).resize(500, 500)
                    .centerCrop().into(image);

            TextView temp = (TextView) forecast.getChildAt(2);
            temp.setText(weatherProvider.getWeatherTemp(i));
        }
        Log.d(TAG,"FINISHED Init Weather....");
    }
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
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //TODO add execute of mycore

                } else {
                    Toast toast = Toast.makeText(this, "Without Internet permission the information shown in the app will be reduced", Toast.LENGTH_LONG);

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG,"no GPS permissions");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1
            );

            return;
        }
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG,"no INTERNET permissions");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE},2
            );

            return;
        }
    }
}

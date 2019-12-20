package com.example.velocok_beta;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class MyLocationListener implements LocationListener  {

    private static final String TAG = "MyLocationListener ";
    private TextView coordinate_textView;
    private String formattedOutput="Lat: %3.4f \nLong: %3.4f";


    MyLocationListener(TextView textView){
        coordinate_textView=textView;
    }

    @Override
    public void onLocationChanged(Location location) {
        String out=String.format(formattedOutput,location.getLatitude(),location.getLongitude());
        Log.d(TAG, out);
     //   coordinate_textView.setText(out);
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

package com.example.velocok_beta;


import android.location.Location;
import android.util.Log;

public class MyPath {
    private static final String TAG = "MyPath";
    private Location[] positionArray;
    private Double speedMax;
    private int i=0;
    private float[] speedArray;

    MyPath(Location[] positions, double speed){
        if (positions.length<2){throw new IllegalArgumentException();}
        positionArray=positions;
        speedMax=speed;
        speedArray=new float[positions.length-1];
    }

    public Location getStart(){
        return positionArray[0];
    }

    public Location getNext(){
        Log.d(TAG, String.valueOf(positionArray.length).concat(" ").concat(String.valueOf(i)));
        if (i+1>positionArray.length){
            return null;
        }
        i++;
        Log.d(TAG,String.valueOf((positionArray[i].getLatitude())));

        return positionArray[i];
    }

    public void setAvgSpeed(float avg){
        speedArray[i]=avg;
    }
    public float[] getAvgArray(){
        return speedArray;
    }

}

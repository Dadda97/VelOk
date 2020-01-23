package com.example.velocok_beta;


import android.icu.text.Transliterator;
import android.location.Location;
import android.util.Log;

public class MyPath {
    private static final String TAG = "MyPath";
    private Location[] positionArray;
    private Double maxSpeed;
    private int i=1;
    private float[] speedArray;

    MyPath(Location[] positions, double speed){
        if (positions.length<2){throw new IllegalArgumentException();}
        positionArray=positions;
        maxSpeed=speed;
        speedArray=new float[positions.length-1];
    }

    public Location getStart(){
        return positionArray[0];
    }

    public Location getEnd(){
        return positionArray[positionArray.length-1];
    }

    public Location getNextCheckpoint(){

        if(i>positionArray.length-1){
            return new Location("");
        }

        return positionArray[i];
    }

    public void setAvgSpeedOfCheckpoint(float avg){
        speedArray[i-1]=avg;
        i++;
    }
    public float[] getAvgArray(){
        return speedArray;
    }

}

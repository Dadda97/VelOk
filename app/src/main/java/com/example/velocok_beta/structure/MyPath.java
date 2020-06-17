package com.example.velocok_beta.structure;


import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DecimalFormat;

public class MyPath implements Parcelable {
    private static final String TAG = "MyPath";
    private Location[] positionArray;
    private Double maxSpeed;
    private String name;
    private int i = 1;
    private float[] speedArray;
    DecimalFormat df = new DecimalFormat("#.00");

    public MyPath(Location[] positions, double speed, String pathName) {
        if (positions.length < 2) {
            throw new IllegalArgumentException();
        }
        positionArray = positions;
        maxSpeed = speed;
        speedArray = new float[positions.length - 1];
        name = pathName;
    }

    private MyPath(Parcel in) {
        int size = in.readInt();
        positionArray = new Location[size];
        in.readTypedArray(positionArray, Location.CREATOR);
        maxSpeed = in.readDouble();
        speedArray = new float[positionArray.length - 1];
        name = in.readString();
    }

    public static final Creator<MyPath> CREATOR = new Creator<MyPath>() {
        @Override
        public MyPath createFromParcel(Parcel in) {
            return new MyPath(in);
        }

        @Override
        public MyPath[] newArray(int size) {
            return new MyPath[size];
        }
    };

    public String getName() {
        return name;
    }

    public Location getStart() {
        return positionArray[0];
    }

    public Location getEnd() {
        return positionArray[positionArray.length - 1];
    }

    public Location getNextCheckpoint() {

        if (i > positionArray.length - 1) {
            return new Location("");
        }

        return positionArray[i];
    }

    public void setAvgSpeedOfCheckpoint(float avg) {
        Log.d(TAG, String.valueOf(avg));
        speedArray[i - 1] = avg;
        i++;
    }

    public String getSectorsMessage() {
        String message = "";
        int countSector = 1;
        for (float avg : speedArray) {
            Log.d(TAG, String.valueOf(avg));
            if (avg > 0) {
                message = message.concat("Settore ".concat(Integer.toString(countSector++)).concat(": ").concat(df.format(avg)).concat(" km/h\n"));
            }
        }
        Log.d(TAG, message);
        return message;
    }

    public float[] getAvgArray() {
        i = 1;
        return speedArray;
    }

    public double getSpeedLimit() {
        return this.maxSpeed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(positionArray.length);
        dest.writeTypedArray(positionArray, flags);
        dest.writeDouble(maxSpeed);
        dest.writeString(name);

    }

}

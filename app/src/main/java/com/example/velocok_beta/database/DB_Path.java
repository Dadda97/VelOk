package com.example.velocok_beta.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Entity
public class DB_Path {


    @PrimaryKey(autoGenerate = true)
    public int uid;

    @TypeConverters({DB_TimestampConverter.class})
    public Date timestamp;


    @TypeConverters({DB_FloatListConverter.class})
    public List<Float> speeds;

    public String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setSpeeds(float[] floatArray) {
        Float[] FloatArray = new Float[floatArray.length];
        int i = 0;
        for (float f : floatArray) {
            FloatArray[i] = floatArray[i];
            i++;
        }
        this.speeds = Arrays.asList(FloatArray);
    }

    public String getName() {
        return this.name;
    }

    public String getDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Rome");
        if (this.timestamp != null) {
            df.setTimeZone(timeZone);
            return df.format(this.timestamp);
        }
        return null;
    }

    public int getUID() {
        return this.uid;
    }


    public List<Float> getSpeeds() {
        if (this.speeds != null) {
            return this.speeds;
        }
        return null;
    }

    public void setDate(Date date) {
        if (date == null) {
            date = new Date();
        }
        this.timestamp = date;
    }
}

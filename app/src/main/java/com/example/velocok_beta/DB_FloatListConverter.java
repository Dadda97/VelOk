package com.example.velocok_beta;

import android.util.Log;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DB_FloatListConverter {
    static String delim=",";
    final static  String TAG= "DB_FloatListConverter";

    @TypeConverter
    public static List<Float> fromSpeeds(String value) {
        if (value != null) {
            String[]stringArray = value.split(delim);
            Float[] floatArray = new Float[stringArray.length];
            int i=0;
            for (String e : stringArray){
                floatArray[i++]=Float.valueOf(e);
            }
            return Arrays.asList(floatArray);
        } else {
            return null;
        }
    }


    @TypeConverter
    public static String toString (List<Float> floatList) {
//        Log.d(TAG,floatList.toString());
        if (floatList != null) {
            String floatString= "";
            for (float e : floatList ){
//                Log.d(TAG,String.valueOf(e));
                floatString=floatString.concat(String.valueOf(e));
                floatString=floatString.concat(delim);
            }
            return floatString.substring(0,floatString.length()-1);
        } else {
            return null;
        }
    }
}

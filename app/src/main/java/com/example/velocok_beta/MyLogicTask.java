package com.example.velocok_beta;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

public class MyLogicTask extends AsyncTask<Integer, Float, Integer> {
    private final String TAG="MyLogicTask";
    MySpeedList speedList;
    AtomicBoolean isUpdated;

    TextView avgSpeedView;
    TextView instantSpeedView;


    MyLogicTask(MySpeedList list, AtomicBoolean atomBool, TextView avg,TextView ins){
        speedList=list;
        isUpdated=atomBool;
        avgSpeedView=avg;
        instantSpeedView=ins;
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        Log.d(TAG,"start task");
        while(true){
            if(isUpdated.compareAndSet(true,false)){
                    publishProgress(speedList.getAverageSpeed(),speedList.getLast());
            }
        }
    }
    @Override
    protected void onProgressUpdate(Float... values) {

        avgSpeedView.setText(String.valueOf(values[0]));
        instantSpeedView.setText(String.valueOf(values[1]));

    }
}

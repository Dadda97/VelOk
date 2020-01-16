package com.example.velocok_beta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MySpeedList {
    private BlockingQueue<Double> list = new LinkedBlockingQueue<>() ;


    public synchronized void add(Double el){
        list.add(el);
    }

    public synchronized float getAverageSpeed(){
        float sum = 0;
        for (Double speed : list){
            sum += speed;
        }
        return sum / list.size();
    }
}

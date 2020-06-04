package com.example.velocok_beta;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MySpeedList {

    private BlockingQueue<Float> list = new LinkedBlockingQueue<>() ;
    private Float last;

    public void add(Float el){
        list.add(el);
        last=el;
    }

    public float getAverageSpeed(){
        float sum = 0;
        for (Float speed : list){
            sum += speed;
        }
        return sum / list.size();
    }
    public float getLast(){
        return last;
    }

    public void clear(){
        list.clear();
    }
}

package com.example.velocok_beta;

import java.util.ArrayList;
import java.util.List;

public class MySpeedList {
    private List<Float> list = new ArrayList<>();


    public void add(Float el){
        list.add(el);
    }

    public float getAverageSpeed(){
        float sum = 0;
        for (float speed : list){
            sum += speed;
        }
        return sum / list.size();
    }
}

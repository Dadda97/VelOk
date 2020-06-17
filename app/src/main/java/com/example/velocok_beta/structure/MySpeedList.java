package com.example.velocok_beta.structure;

import java.util.LinkedList;
import java.util.List;

public class MySpeedList {

    private List<Float> list = new LinkedList<>();

    public void add(Float el) {
        list.add(el);
    }

    public float getAverageSpeed() {
        float sum = 0;
        for (Float speed : list) {
            sum += speed;
        }
        return sum / list.size();
    }

    public void clear() {
        list.clear();
    }
}

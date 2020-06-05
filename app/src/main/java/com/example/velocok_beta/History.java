package com.example.velocok_beta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    public static String TAG = "HISTORY";
    TextView t = null;
    List<DB_Path> paths = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        MyDatabase DB = new MyDatabase(getApplicationContext());

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        RVAdapter adapter = new RVAdapter(paths);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        DB.getAllForAdapter(adapter);
        t = findViewById(R.id.pathDate);
        Integer recs_N = 0;

        Log.d(TAG, recs_N.toString());

    }
}

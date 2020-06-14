package com.example.velocok_beta;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        RecyclerView rv = findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        RVAdapter adapter = new RVAdapter(paths);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        DB.getAllForAdapter(adapter);


    }
}

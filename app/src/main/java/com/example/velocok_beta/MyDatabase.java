package com.example.velocok_beta;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.preference.PreferenceManager;
import androidx.room.Room;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase {
    final static  String TAG="MyDatabase";

    private static DB_AppDatabase database;


    public MyDatabase(Context context) {
        database = Room.databaseBuilder(context, DB_AppDatabase.class, "path-database").fallbackToDestructiveMigration().build();

    }
    public void insert(DB_Path obj){
        new insertAllTask().execute(obj);
    }

    public void insert(MyPath myPath){
        final DB_Path obj=new DB_Path();
        obj.setName(myPath.getName());
        obj.setSpeeds(myPath.getAvgArray());
        obj.setDate(null);
        this.insert(obj);
    }

    public void insert(String name, float[]speeds){
        final DB_Path obj=new DB_Path();
        obj.setName(name);
        obj.setDate(null);
        obj.setSpeeds(speeds);
        this.insert(obj);
    }

    public void getAllForAdapter(RVAdapter adapter){
        new getAllForAdapterTask(adapter).execute();
    }

    public void getAll() {
        new getAllTask().execute();
    }

    public void deleteAll(){
        new deleteAllTask().execute();
    }

    static class deleteAllTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            database.pathDao().deleteAll();;
            return null;
        }
    }

    static class insertAllTask extends AsyncTask<DB_Path, Void, Void> {
        @Override
        protected Void doInBackground(DB_Path... db_paths) {
            database.pathDao().insertAll(db_paths);
            return null;
        }
    }

    static class getAllForAdapterTask extends AsyncTask<Void, Void, List<DB_Path>>{

        RVAdapter myAdapter;

        public getAllForAdapterTask(RVAdapter adapter) {
            this.myAdapter = adapter;
        }

        @Override
        protected List<DB_Path> doInBackground(Void... voids) {
            return database.pathDao().getAll();
        }

        @Override
        protected void onPostExecute(List<DB_Path> db_paths) {
            myAdapter.setNewPaths(db_paths);
            for (DB_Path path : db_paths){
                Log.d(TAG,String.valueOf(path.getUID()));
                Log.d(TAG,path.getName());
                Log.d(TAG,path.getDate());
                List<Float> speeds = path.getSpeeds();
                if (speeds !=null) {
                    for (Float e : path.getSpeeds()) {
                        Log.d(TAG, String.valueOf(e));
                    }
                }
            }
        }
    }

    static class getAllTask extends AsyncTask<Void, Void, List<DB_Path>>{

        @Override
        protected List<DB_Path> doInBackground(Void... voids) { ;
            return database.pathDao().getAll();
        }

        @Override
        protected void onPostExecute(List<DB_Path> db_paths) {
            for (DB_Path path : db_paths){
                Log.d(TAG,String.valueOf(path.getUID()));
                Log.d(TAG,path.getName());
                Log.d(TAG,path.getDate());
                List<Float> speeds = path.getSpeeds();
                if (speeds !=null) {
                    for (Float e : path.getSpeeds()) {
                        Log.d(TAG, String.valueOf(e));
                    }
                }
            }
        }
    }

}



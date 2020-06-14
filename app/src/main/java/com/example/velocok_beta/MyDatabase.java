package com.example.velocok_beta;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import java.util.List;

public class MyDatabase {
    final static String TAG = "MyDatabase";

    private static DB_AppDatabase database;


    public MyDatabase(Context context) {
        database = Room.databaseBuilder(context, DB_AppDatabase.class, "path-database").fallbackToDestructiveMigration().build();

    }

    public void insert(DB_Path obj) {
        new insertAllTask().execute(obj);
    }

    public void insert(MyPath myPath) {
        final DB_Path obj = new DB_Path();
        obj.setName(myPath.getName());
        obj.setSpeeds(myPath.getAvgArray());
        obj.setDate(null);
        this.insert(obj);
    }

    public void getAllForAdapter(RVAdapter adapter) {
        new getAllForAdapterTask(adapter).execute();
    }

    public void getAll() {
        new getAllTask().execute();
    }


    static class insertAllTask extends AsyncTask<DB_Path, Void, Void> {
        @Override
        protected Void doInBackground(DB_Path... db_paths) {
            database.pathDao().insertAll(db_paths);
            return null;
        }
    }

    static class getAllForAdapterTask extends AsyncTask<Void, Void, List<DB_Path>> {

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
        }
    }

    static class getAllTask extends AsyncTask<Void, Void, List<DB_Path>> {

        @Override
        protected List<DB_Path> doInBackground(Void... voids) {
            return database.pathDao().getAll();
        }

        @Override
        protected void onPostExecute(List<DB_Path> db_paths) {}
    }

}



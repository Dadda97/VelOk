package com.example.velocok_beta;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DB_Path.class}, version = 2, exportSchema = false)
public abstract class DB_AppDatabase extends RoomDatabase {
    public abstract DB_Dao pathDao();
}

package com.example.velocok_beta.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DB_Dao {
    @Query("SELECT * FROM DB_Path")
    List<DB_Path> getAll();

    @Query("SELECT * FROM DB_Path WHERE uid = :UID")
    List<DB_Path> getByIds(int UID);

    @Insert
    void insertAll(DB_Path... path);

    @Query("DELETE FROM DB_Path")
    void deleteAll();

    @Delete
    void delete(DB_Path path);

}

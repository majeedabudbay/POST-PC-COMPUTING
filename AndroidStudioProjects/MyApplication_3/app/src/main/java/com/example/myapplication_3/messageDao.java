package com.example.myapplication_3;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface messageDao {
    @Query("SELECT * FROM message")
    List<message> getAll();

    @Query("SELECT COUNT(*) from message")
    int countMessages();

    @Insert
    void insertAll(message... messages);

    @Delete
    void delete(message message);
}

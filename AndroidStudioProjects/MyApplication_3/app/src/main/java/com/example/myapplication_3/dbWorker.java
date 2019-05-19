package com.example.myapplication_3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.room.Room;

public class dbWorker {
     AppDatabase database;
     Executor executor = Executors.newSingleThreadExecutor();



    public dbWorker(Context context){
        database = Room.databaseBuilder(context, AppDatabase.class, "message")
                .build();

    }

    public interface GetAllCallback {
        public void resultsReady(List<message> result);
    }

    public void getAll(final GetAllCallback callback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<message> messages = database.messageDao().getAll();
                callback.resultsReady(messages);
            }
        });

    }

    public void countMessages(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int msgs = database.messageDao().countMessages();
                Log.d("size of the msg list", String.valueOf(msgs));

            }
        });
    }

    public void insertAll(final message... messages){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.messageDao().insertAll(messages);
            }
        });
    }


    public void delete(final message message){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.messageDao().delete(message);
            }
        });

    }
}
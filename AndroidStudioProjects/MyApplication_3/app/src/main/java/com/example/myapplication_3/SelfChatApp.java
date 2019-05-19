package com.example.myapplication_3;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

public class SelfChatApp extends Application {

    public messageDao messageDao;
        public dbWorker dbWorker;

    @Override
    public void onCreate() {
        super.onCreate();
        dbWorker = new dbWorker(getApplicationContext());

        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "message")
                .allowMainThreadQueries().build();
        this.messageDao = database.messageDao();
        dbWorker.countMessages();
    }
}

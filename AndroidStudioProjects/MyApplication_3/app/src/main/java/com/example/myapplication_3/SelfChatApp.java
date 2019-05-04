package com.example.myapplication_3;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

public class SelfChatApp extends Application {

    public messageDao messageDao;

    @Override
    public void onCreate() {
        super.onCreate();
        final AppDatabase db =
                Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "message")
                        .allowMainThreadQueries()
                        .build();

        this.messageDao = db.messageDao();
        Log.d("size of the msg list", String.valueOf(this.messageDao.countMessages()));
    }
}

package com.example.myapplication;

import android.app.Application;
import android.provider.SyncStateContract;

import androidx.room.Room;

public class AppController extends Application {

    private static AppController mInstance;

    private static AppDatabase appDatabase;

    public static AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, AppDatabase.db_name).build();
    }

    public static AppDatabase getAppDatabase(){
        return appDatabase;
    }
}


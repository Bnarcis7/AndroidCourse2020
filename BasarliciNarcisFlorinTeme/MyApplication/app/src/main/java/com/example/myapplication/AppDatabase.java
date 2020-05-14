package com.example.myapplication;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public static AppDatabase instance;
    public abstract UserDao userDao();
    public static final String db_name = "database";

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = createDb(context);
        }
        return instance;
    }

    private static AppDatabase createDb(final Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                db_name).build();
    }
}

package com.example.myapplication;

import android.content.Context;

public class UserRepository {
    private AppDatabase appDatabase;

    public UserRepository(Context context) {
        appDatabase = AppController.getAppDatabase();
    }

    public User insertUser(String firstName, String lastName) {
        return appDatabase.userDao().insert(firstName, lastName);
   }

    public User deleteUser(String firstName, String lastName){
        return appDatabase.userDao().delete(firstName,lastName);
    }

}

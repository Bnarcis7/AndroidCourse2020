package com.example.doctorhowproject.Application;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

// Override for realm initialization
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this.getApplicationContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().name("RealmData.realm").build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}

package com.example.doctorhowproject.Application;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

//SET REALM CONFIGURATION WHEN APP STARTS AND ITS AVAILABLE FOR THE REST OF THE APP LIFETIME
public class MyApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this.getApplicationContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().name("RealmData.realm").build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}

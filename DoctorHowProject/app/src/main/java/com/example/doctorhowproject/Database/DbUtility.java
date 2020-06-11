package com.example.doctorhowproject.Database;
import android.content.Context;
import com.example.doctorhowproject.Models.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

public class DbUtility {
    private Context context;
    private RealmConfiguration realmConfig = new RealmConfiguration.Builder().name("RealmData.realm").build();
    private Realm realm = Realm.getInstance(realmConfig);

    public DbUtility(Context context) {
        this.context = context;
    }

    public boolean createUser(User user) {
        try {
            realm.beginTransaction();
            realm.copyToRealm(user);
            realm.commitTransaction();
            realm.close();
            return true;
        } catch (RealmException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String email) {
        try {
            RealmResults<User> user = realm.where(User.class).equalTo(DbConstants.EMAIL_KEY, email)
                    .findAll();
            user.clear();
            return true;
        } catch (RealmException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User readUser(String email) {
        User user = realm.where(User.class).equalTo(DbConstants.EMAIL_KEY, email)
                .findFirst();
        return user;
    }

    public boolean isUserExist(String email) {
        try {
            RealmResults<User> user = realm.where(User.class).equalTo(DbConstants.EMAIL_KEY, email)
                    .findAll();
            if (user.size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (RealmException e) {
            e.printStackTrace();
            return false;
        }
    }
}
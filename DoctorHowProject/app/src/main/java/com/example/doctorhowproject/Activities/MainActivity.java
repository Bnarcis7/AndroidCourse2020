package com.example.doctorhowproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import com.example.doctorhowproject.Fragments.LoginFragment;
import com.example.doctorhowproject.Fragments.RegisterFragment;
import com.example.doctorhowproject.R;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goToLogin();
    }

    private void goToLogin() {
        LoginFragment fragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, "login_fragment")
                .commit();

    }
}

package com.example.doctorhowproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.doctorhowproject.Fragments.LoginFragment;
import com.example.doctorhowproject.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goToLogin();
    }

    private void goToLogin(){
        LoginFragment fragment=new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main,fragment,"login_fragment")
                .commit();
    }
}

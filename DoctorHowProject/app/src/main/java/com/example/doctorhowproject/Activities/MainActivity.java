package com.example.doctorhowproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.doctorhowproject.Fragments.LoginFragment;
import com.example.doctorhowproject.Fragments.RegisterFragment;
import com.example.doctorhowproject.R;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActivity {

    @InjectView(R.id.login_btn)
    protected Button loginBtn;
    @InjectView(R.id.register_btn)
    protected Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clickListener();
    }

    private void clickListener() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().add(R.id.fragment_container
                        , new LoginFragment()).commit();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().add(R.id.fragment_container
                        , new RegisterFragment()).commit();
            }
        });
    }
}

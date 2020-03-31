package com.example.homeworkone.activities;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import com.example.homeworkone.R;
import com.example.homeworkone.fragments.Fragment1Activity1;

public class SecondActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fragment1_activity2);

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            Fragment1Activity1 firstFragment = new Fragment1Activity1();
            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }
}
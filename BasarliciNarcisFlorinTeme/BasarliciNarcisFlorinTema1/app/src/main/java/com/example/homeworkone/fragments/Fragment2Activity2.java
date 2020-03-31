package com.example.homeworkone.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.homeworkone.R;


public class Fragment2Activity2 extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment2_activity2, container, false);
        Button btnThirdFragment1 = (Button) view.findViewById(R.id.btn_fragment3_1);

        btnThirdFragment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Fragment3Activity2());
                fr.addToBackStack(null);
                fr.commit();
            }
        });

        Button btnThirdFragment2 = (Button) view.findViewById(R.id.btn2_fragment3_2);
        btnThirdFragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            //i didnt understand the assignment for removing
            public void onClick(View v) {
                Fragment fragment = getFragmentManager().findFragmentByTag("FRAGMENT1_ACTIVITY2");
                if (fragment != null)
                    getFragmentManager().beginTransaction().remove(fragment).commit();
                //getFragmentManager().popBackStack(getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount()-2).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        Button btnThirdFragment3 = (Button) view.findViewById(R.id.btn3_fragment3_3);
        btnThirdFragment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }
}




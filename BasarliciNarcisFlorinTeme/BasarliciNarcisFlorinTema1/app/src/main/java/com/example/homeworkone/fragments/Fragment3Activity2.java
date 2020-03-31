package com.example.homeworkone.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.homeworkone.R;
import com.example.homeworkone.activities.MainActivity;
import com.example.homeworkone.activities.SecondActivity;


public class Fragment3Activity2 extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment3_activity2, container, false);
        return view;
    }

    public void onClick() {
        getActivity().finish();
        System.exit(0);
    }
}

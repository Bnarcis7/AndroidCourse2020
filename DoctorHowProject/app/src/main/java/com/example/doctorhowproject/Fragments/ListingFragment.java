package com.example.doctorhowproject.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doctorhowproject.Models.Listing;
import com.example.doctorhowproject.Models.User;
import com.example.doctorhowproject.R;

import java.util.ArrayList;

public class ListingFragment extends Fragment {

    private User mUser;
    private Listing mListing;
    private ArrayList<Bitmap> images;
    private FragmentActivity mActivity;

    public ListingFragment() {

    }

    public ListingFragment(User user, Listing listing) {
        this.mUser = user;
        this.mListing = listing;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();
        return inflater.inflate(R.layout.fragment_listing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnNext=mActivity.findViewById(R.id.btn_img_next);
        TextView title = mActivity.findViewById(R.id.listing_title);
        final ImageView imageView =mActivity.findViewById(R.id.listing_images);

        //TODO FIX STUFF HERE
        Animation in = AnimationUtils.loadAnimation(getContext(),R.anim.in);
        Animation out=AnimationUtils.loadAnimation(getContext(),R.anim.out);

        imageView.setAnimation(in);

        final int[] i = {0};
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(images.get(i[0] %images.size()));
                i[0]++;
            }
        });



        title.setText(mListing.getTitle());
    }



}
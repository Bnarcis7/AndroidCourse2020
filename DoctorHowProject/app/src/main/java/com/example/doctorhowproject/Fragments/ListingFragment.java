package com.example.doctorhowproject.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doctorhowproject.Adapters.ImageAdapter;
import com.example.doctorhowproject.Models.Listing;
import com.example.doctorhowproject.Models.User;
import com.example.doctorhowproject.R;

import java.util.ArrayList;

public class ListingFragment extends Fragment {

    private Listing mListing;
    private FragmentActivity mActivity;

    public ListingFragment() {}

    public ListingFragment(Listing listing) {
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

        TextView title = mActivity.findViewById(R.id.listing_title);
        TextView phone = mActivity.findViewById(R.id.listing_phone);
        TextView description = mActivity.findViewById(R.id.listing_description);
        ViewPager viewPager = mActivity.findViewById(R.id.listing_images);
        ImageAdapter imageAdapter =new ImageAdapter(getContext(),mListing.getImagesPaths());
        viewPager.setAdapter(imageAdapter);
        title.setText(mListing.getTitle());
        phone.setText(mListing.getPhone());
        description.setText(mListing.getDetails());
    }


}
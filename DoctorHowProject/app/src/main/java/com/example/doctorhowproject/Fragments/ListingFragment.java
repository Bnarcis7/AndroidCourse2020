package com.example.doctorhowproject.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private FragmentActivity mActivity;

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

        Button btnNext = mActivity.findViewById(R.id.btn_img_next);
        TextView title = mActivity.findViewById(R.id.listing_title);
        ImageView imageView = mActivity.findViewById(R.id.listing_images);

        title.setText(mListing.getTitle());
        Bitmap bmImg = BitmapFactory.decodeFile(mListing.getImagesPaths().get(0));
        imageView.setImageBitmap(bmImg);
    }
}
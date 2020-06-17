package com.example.doctorhowproject.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.doctorhowproject.Adapters.ImageAdapter;
import com.example.doctorhowproject.Models.Listing;
import com.example.doctorhowproject.R;

public class ListingFragment extends Fragment {

    private Listing mListing;
    private FragmentActivity mActivity;

    public ListingFragment() {
    }

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
        final TextView phone = mActivity.findViewById(R.id.listing_phone);
        TextView description = mActivity.findViewById(R.id.listing_description);

        ViewPager viewPager = mActivity.findViewById(R.id.listing_images);
        Button callBtn = mActivity.findViewById(R.id.listing_call_button);

        ImageAdapter imageAdapter = new ImageAdapter(getContext(), mListing.getImagesPaths());
        viewPager.setAdapter(imageAdapter);

        title.setText(mListing.getTitle());
        phone.setText(mListing.getPhone());
        description.setText(mListing.getDetails());

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+" + phone.getText().toString().trim()));
                startActivity(callIntent);
            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+" + phone.getText().toString().trim()));
                startActivity(callIntent);
            }
        });
    }
}
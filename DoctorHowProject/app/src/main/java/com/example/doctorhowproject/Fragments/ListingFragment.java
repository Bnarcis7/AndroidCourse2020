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
import androidx.viewpager.widget.ViewPager;

import com.example.doctorhowproject.Activities.HomePageActivity;
import com.example.doctorhowproject.Adapters.ImageAdapter;
import com.example.doctorhowproject.Models.Listing;
import com.example.doctorhowproject.R;

import java.io.File;

import io.realm.Realm;

public class ListingFragment extends Fragment {

    private Listing mListing;
    private HomePageActivity mActivity;
    private Realm mRealm;

    public ListingFragment() {
    }

    public ListingFragment(Listing listing) {
        this.mListing = listing;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (HomePageActivity) getActivity();
        mRealm = Realm.getDefaultInstance();
        return inflater.inflate(R.layout.fragment_listing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView title = mActivity.findViewById(R.id.listing_title);
        final TextView phone = mActivity.findViewById(R.id.listing_phone);
        TextView email = mActivity.findViewById(R.id.listing_email);
        TextView description = mActivity.findViewById(R.id.listing_description);


        ViewPager viewPager = mActivity.findViewById(R.id.listing_images);
        Button callBtn = mActivity.findViewById(R.id.listing_call_button);
        Button emailBtn = mActivity.findViewById(R.id.listing_email_button);
        Button removeBtn = mActivity.findViewById(R.id.listing_remove_button);
        Button favoriteBtn = mActivity.findViewById(R.id.listing_favorite_button);

        ImageAdapter imageAdapter = new ImageAdapter(getContext(), mListing.getImagesPaths());
        viewPager.setAdapter(imageAdapter);

        title.setText(mListing.getTitle());
        phone.setText(mListing.getPhone());
        description.setText(mListing.getDetails());
        email.setText(mListing.getOwner().getEmail());

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+" + phone.getText().toString().trim()));
                startActivity(callIntent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mListing.getOwner().getEmail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, mListing.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, "Buna ziua! As dori mai multe informatii legate de anunt.");
                startActivity(Intent.createChooser(intent, ""));
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

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mListing.getOwner().getEmail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, mListing.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, "Buna ziua! As dori mai multe informatii legate de anunt.");
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mActivity.getFavorites().contains(mListing))
                    mActivity.getFavorites().add(mListing);
            }
        });

        if (mActivity.getUser().getId().equals(mListing.getOwner().getId())) {
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeListing();
                    goToHome();
                }
            });
        } else {
            removeBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void removeListing() {
        // Deleted images from image folder for this listing
        for (String i : mListing.getImagesPaths()) {
            File file = new File(i);
            if (file.exists()) {
                file.delete();
            }
        }

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.where(Listing.class)
                        .equalTo("id", mListing.getId()).findFirst().deleteFromRealm();
            }
        });
    }

    private void goToHome() {
        HomeDefaultFragment fragment = new HomeDefaultFragment();

        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
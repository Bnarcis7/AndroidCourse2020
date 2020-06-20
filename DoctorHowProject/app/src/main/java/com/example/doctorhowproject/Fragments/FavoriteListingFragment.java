package com.example.doctorhowproject.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.doctorhowproject.Activities.HomePageActivity;
import com.example.doctorhowproject.Activities.MapActivity;
import com.example.doctorhowproject.Adapters.ImageAdapter;
import com.example.doctorhowproject.Models.Listing;
import com.example.doctorhowproject.R;
import com.example.doctorhowproject.Utils.GenericConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class FavoriteListingFragment extends Fragment {
    private HomePageActivity mActivity;
    private Listing mListing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (HomePageActivity) getActivity();
        return inflater.inflate(R.layout.fragment_favorite_listing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView title = mActivity.findViewById(R.id.favorite_listing_title);
        final TextView phone = mActivity.findViewById(R.id.favorite_listing_phone);
        TextView email = mActivity.findViewById(R.id.favorite_listing_email);
        TextView description = mActivity.findViewById(R.id.favorite_listing_description);
        TextView address = mActivity.findViewById(R.id.favorite_listing_address);

        ViewPager viewPager = mActivity.findViewById(R.id.favorite_listing_images);
        Button callBtn = mActivity.findViewById(R.id.favorite_listing_call_button);
        Button emailBtn = mActivity.findViewById(R.id.favorite_listing_email_button);
        Button removeBtn = mActivity.findViewById(R.id.favorite_listing_remove_button);

        ImageAdapter imageAdapter = new ImageAdapter(getContext(), mListing.getImagesPaths());
        viewPager.setAdapter(imageAdapter);

        title.setText(mListing.getTitle());
        phone.setText(mListing.getPhone());
        description.setText(mListing.getDetails());
        email.setText(mListing.getOwner().getEmail());
        address.setText(mListing.getAddress());

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

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?f=d&daddr="+mListing.getAddress()));
                intent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));
                if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
                    startActivity(intent);
                }*/
                if (isServicesOk()) {
                    Intent intent = new Intent(mActivity, MapActivity.class);
                    intent.putExtra("address", mListing.getAddress());
                    intent.putExtra("title", mListing.getTitle());
                    startActivity(intent);
                }
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

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeListing();
                goToFavorites();
            }
        });
    }

    private void removeListing() {
        mActivity.getFavorites().remove(mListing);
    }

    private void goToFavorites() {
        FavoritesFragment fragment = new FavoritesFragment();

        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private boolean isServicesOk() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());

        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(mActivity, available, GenericConstants.ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getContext(), "Cant open maps", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public FavoriteListingFragment() {
    }

    public FavoriteListingFragment(Listing listing) {
        mListing = listing;
    }
}

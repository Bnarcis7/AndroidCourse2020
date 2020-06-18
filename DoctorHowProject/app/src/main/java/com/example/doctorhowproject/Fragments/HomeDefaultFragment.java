package com.example.doctorhowproject.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.doctorhowproject.Activities.HomePageActivity;
import com.example.doctorhowproject.Adapters.ListingsAdapter;
import com.example.doctorhowproject.Models.Listing;
import com.example.doctorhowproject.R;
import com.example.doctorhowproject.Utils.GenericConstants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomeDefaultFragment extends Fragment {

    private HomePageActivity mActivity;
    private Realm mRealm;
    private ArrayList<Listing> mListings;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (HomePageActivity) getActivity();
        mRealm = Realm.getDefaultInstance();
        mRealm.refresh();
        return inflater.inflate(R.layout.fragment_home_default, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check for storage permission
        if (!checkStoragePermission()) {
            requestStoragePermission();
        }

        // Get the listings from database and build the recycler view
        mListings = new ArrayList<>();
        refreshListings();
        buildRecyclerView();

        FloatingActionButton floatingBtn = mActivity.findViewById(R.id.floating_button);
        final SwipeRefreshLayout swipeRefreshLayout = mActivity.findViewById(R.id.home_refresh_layout);

        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewListingFragment fragment = new NewListingFragment();
                mActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, "new_listing_fragment")
                        .addToBackStack(fragment.toString())
                        .commit();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshListings();
                mRecyclerView.setAdapter(new ListingsAdapter(mListings));
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        // Code for permission check and show dialog in case there is none
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission needed")
                    .setMessage("We need this permission so we can read the listings from internal storage")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(mActivity,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    GenericConstants.READ_STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    GenericConstants.READ_STORAGE_PERMISSION_CODE);
        }
    }


    private void buildRecyclerView() {
        ListingsAdapter adapter = new ListingsAdapter(mListings);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);

        mRecyclerView = mActivity.findViewById(R.id.recycler_view_listings);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(new ListingsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Listing selectedListing = mListings.get(position);
                ListingFragment fragment = new ListingFragment(selectedListing);
                mActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right)
                        .replace(R.id.fragment_container, fragment, "listing_fragment")
                        .addToBackStack(fragment.toString())
                        .commit();
            }
        });
    }

    private void refreshListings() {
        mListings.clear();
        mListings.addAll(getAllListings());
    }

    private ArrayList<Listing> getAllListings() {
        RealmResults<Listing> query = mRealm.where(Listing.class)
                .findAll();

        return new ArrayList<Listing>(query);
    }
}
package com.example.doctorhowproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doctorhowproject.Activities.HomePageActivity;
import com.example.doctorhowproject.Adapters.ListingsAdapter;
import com.example.doctorhowproject.Models.Listing;
import com.example.doctorhowproject.R;
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
        return inflater.inflate(R.layout.fragment_home_default, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the listings from database and build the recycler view
        mListings = new ArrayList<>();
        refreshListings();
        buildRecyclerView();

        FloatingActionButton floatingBtn = mActivity.findViewById(R.id.floating_button);

        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewListingFragment fragment = new NewListingFragment();
                mActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, "new_listing_fragment")
                        .commit();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
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
                ListingFragment fragment = new ListingFragment(mActivity.getUser(), selectedListing);
                mActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment, "listing_fragment")
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
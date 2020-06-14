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
    private Realm realm;
    private ArrayList<Listing> listings;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity =(HomePageActivity) getActivity();
        realm = Realm.getDefaultInstance(); // GET AN INSTANCE FOR THIS THREAD ONLY!
        return inflater.inflate(R.layout.fragment_home_default, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listings=new ArrayList<>();
        refreshListings();
        buildRecyclerView();

        FloatingActionButton floatingBtn=mActivity.findViewById(R.id.floating_button);
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewListingFragment fragment=new NewListingFragment();
                mActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment,"new_listing_fragment")
                        .commit();
            }
        });
    }

    private void buildRecyclerView(){
        recyclerView = mActivity.findViewById(R.id.recycler_view_listings);
        ListingsAdapter adapter=new ListingsAdapter(listings);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setOnItemClickListener(new ListingsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Listing selectedListing = listings.get(position);
                ListingFragment fragment=new ListingFragment(mActivity.user,selectedListing);
                mActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment,"listing_fragment")
                        .commit();
            }
        });
    }

    private void refreshListings(){
        listings.clear();
        listings.addAll(getAllListings());
    }

    private ArrayList<Listing> getAllListings() {
        RealmResults<Listing> query = realm.where(Listing.class)
            .findAll();

        return new ArrayList<Listing>(query);
    }

    private void remove(){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }
}
package com.example.doctorhowproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doctorhowproject.Adapters.ListingsAdapter;
import com.example.doctorhowproject.Models.Listing;
import com.example.doctorhowproject.R;

import java.util.ArrayList;

import io.realm.Realm;

public class ListingsFragment extends Fragment {
    private FragmentActivity mActivity;
    private Realm realm;
    private ArrayList<Listing> listings;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();
        realm = Realm.getDefaultInstance(); // GET AN INSTANCE FOR THIS THREAD ONLY!
        return inflater.inflate(R.layout.fragment_listings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listings=new ArrayList<>();
        listings.add(new Listing("listingTest1"));
        listings.add(new Listing("listingTest2"));
        listings.add(new Listing("listingTest3"));
        listings.add(new Listing("listingTest4"));

        recyclerView = mActivity.findViewById(R.id.recycler_view_listings);
        ListingsAdapter adapter=new ListingsAdapter(listings);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}
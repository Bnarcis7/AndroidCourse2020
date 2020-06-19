package com.example.doctorhowproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.doctorhowproject.Activities.HomePageActivity;
import com.example.doctorhowproject.Adapters.ListingsAdapter;
import com.example.doctorhowproject.Models.Listing;
import com.example.doctorhowproject.R;

import java.util.ArrayList;

import io.realm.Realm;

public class FavoritesFragment extends Fragment {

    private HomePageActivity mActivity;
    private Realm mRealm;
    private ListingsAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (HomePageActivity) getActivity();
        mRealm = Realm.getDefaultInstance();
        mRealm.refresh();
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new ListingsAdapter(mActivity.getFavorites());
        buildRecyclerView();
    }

    private void buildRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);

        mRecyclerView = mActivity.findViewById(R.id.recycler_view_favorites);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter.setOnItemClickListener(new ListingsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Listing selectedListing = mActivity.getFavorites().get(position);
                FavoriteListingFragment fragment = new FavoriteListingFragment(selectedListing);
                mActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right)
                        .replace(R.id.fragment_container, fragment, "favorite_listing_fragment")
                        .addToBackStack(fragment.toString())
                        .commit();
            }
        });
    }
}
package com.example.doctorhowproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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

public class FavoritesFragment extends Fragment {

    private HomePageActivity mActivity;
    private ArrayList<Listing> mFilteredListings;
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
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SwipeRefreshLayout swipeRefreshLayout = mActivity.findViewById(R.id.favorites_refresh);

        mFilteredListings = new ArrayList<>(mActivity.getFavorites());
        mAdapter = new ListingsAdapter(mFilteredListings);
        buildRecyclerView();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFilteredListings.clear();
                mFilteredListings.addAll(mActivity.getFavorites());
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuInflater menuInflater = mActivity.getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterListings(s);
                mAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0) {
                    getAllListings();
                    mAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    private void getAllListings() {
        mFilteredListings.clear();
        mFilteredListings.addAll(mActivity.getFavorites());
    }

    private void filterListings(final String charSequence) {
        mFilteredListings.clear();
        for (Listing i : mActivity.getFavorites()) {
            if (i.getTitle().toLowerCase().trim().contains(charSequence.toLowerCase().trim())) {
                mFilteredListings.add(i);
            }
        }


    }
}
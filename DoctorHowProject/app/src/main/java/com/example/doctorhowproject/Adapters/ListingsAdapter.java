package com.example.doctorhowproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorhowproject.Models.Listing;
import com.example.doctorhowproject.R;
import com.example.doctorhowproject.ViewHolders.ListingsViewHolder;

import java.util.ArrayList;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsViewHolder> {

    private ArrayList<Listing> listings;

    public ListingsAdapter(ArrayList<Listing> listings){
            this.listings=listings;
    }

    @NonNull
    @Override
    public ListingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout,parent,false);
        return new ListingsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingsViewHolder holder, int position) {
        Listing currentListing=listings.get(position);
        holder.title.setText(currentListing.getTitle());
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }
}

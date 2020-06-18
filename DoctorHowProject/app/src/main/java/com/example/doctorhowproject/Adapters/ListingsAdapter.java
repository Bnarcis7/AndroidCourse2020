package com.example.doctorhowproject.Adapters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorhowproject.Models.Listing;
import com.example.doctorhowproject.R;
import com.example.doctorhowproject.ViewHolders.ListingsViewHolder;

import java.util.ArrayList;


public class ListingsAdapter extends RecyclerView.Adapter<ListingsViewHolder> implements Filterable {

    public ArrayList<Listing> listings;
    public ArrayList<Listing> listingsFull;
    private ViewGroup parent;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ListingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        this.parent = parent;
        return new ListingsViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingsViewHolder holder, int position) {
        Listing currentListing = listings.get(position);
        holder.title.setText(currentListing.getTitle());

        if (currentListing.getImagesPaths().size() != 0) {
            Bitmap bmImg = BitmapFactory.decodeFile(currentListing.getImagesPaths().get(0));
            bmImg = Bitmap.createScaledBitmap(bmImg, 500, 500, true);
            holder.image.setImageBitmap(bmImg);
        }
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    private Filter listFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Listing> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length()==0){
                filteredList.addAll(listingsFull);
            }
            else{
                String filterPattern=charSequence.toString().toLowerCase().trim();

                for(Listing i : listingsFull){
                    if(i.getTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(i);
                    }
                }
            }

            FilterResults results=new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listings.clear();
            listings.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ListingsAdapter(ArrayList<Listing> listings) {
        this.listings = listings;
        listingsFull= new ArrayList<>(listings);
    }
}

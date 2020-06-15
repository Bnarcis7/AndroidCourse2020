package com.example.doctorhowproject.Adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorhowproject.Models.Listing;
import com.example.doctorhowproject.R;
import com.example.doctorhowproject.ViewHolders.ListingsViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.doctorhowproject.Utils.Utils.arrayToImage;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsViewHolder>{

    public ArrayList<Listing> listings;
    private ViewGroup parent;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public ListingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout,parent,false);
        this.parent=parent;
        return new ListingsViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingsViewHolder holder, int position) {
        Listing currentListing=listings.get(position);
        holder.title.setText(currentListing.getTitle());
        Bitmap bitmap =arrayToImage(currentListing.getListingImages().get(0).getImgArray());
        holder.image.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public ListingsAdapter(ArrayList<Listing> listings){
        this.listings=listings;
    }
}

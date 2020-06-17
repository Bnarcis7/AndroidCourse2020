package com.example.doctorhowproject.Adapters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorhowproject.Models.Listing;
import com.example.doctorhowproject.R;
import com.example.doctorhowproject.ViewHolders.ListingsViewHolder;


import java.util.ArrayList;



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
        if(currentListing.getImagesPaths().size()!=0) {
            Bitmap bmImg = BitmapFactory.decodeFile(currentListing.getImagesPaths().get(0));
            holder.image.setImageBitmap(bmImg);
        }
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public ListingsAdapter(ArrayList<Listing> listings){
        this.listings=listings;
    }
}

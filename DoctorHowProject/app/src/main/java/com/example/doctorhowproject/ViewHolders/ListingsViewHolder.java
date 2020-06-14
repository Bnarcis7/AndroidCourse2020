package com.example.doctorhowproject.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.doctorhowproject.R;

public class ListingsViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;
    public TextView title;

    public ListingsViewHolder(@NonNull View itemView) {
        super(itemView);
        image= itemView.findViewById(R.id.recycler_image);
        title=itemView.findViewById(R.id.recycler_title);
    }


}

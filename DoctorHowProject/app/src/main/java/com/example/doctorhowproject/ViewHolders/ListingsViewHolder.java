package com.example.doctorhowproject.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorhowproject.Adapters.ListingsAdapter;
import com.example.doctorhowproject.R;

public class ListingsViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;
    public TextView title;

    public ListingsViewHolder(@NonNull final View itemView, final ListingsAdapter.OnItemClickListener listener) {
        super(itemView);
        image = itemView.findViewById(R.id.recycler_images);
        title = itemView.findViewById(R.id.recycler_title);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }
}

package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter {
    private Context context;
    private List<User> userList;
    @NonNull
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.user_view_layout,parent,false);
        UserViewHolder userViewHolder = new UserViewHolder(textView);
        return userViewHolder;
    }
    public int getItemCount() {
        return this.userList.size();
    }

    public final User getItem(int pos) {
        return (User)this.userList.get(pos);
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView firstName;
        public UserViewHolder(TextView firstName) {
            super(firstName);
            this.firstName = firstName;
        }
    }
}

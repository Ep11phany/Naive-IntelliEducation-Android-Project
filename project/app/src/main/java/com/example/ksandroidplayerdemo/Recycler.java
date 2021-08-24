package com.example.ksandroidplayerdemo;

import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;




public class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

    private List<Item> mItemList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        public ViewHolder(View view) {
            super(view);
            Name = (TextView) view.findViewById(R.id.Subject);
        }

    }

    public Recycler(List<Item> ItemList) {
        mItemList = ItemList;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_button, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mItemList.get(position);
        holder.Name.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }



}
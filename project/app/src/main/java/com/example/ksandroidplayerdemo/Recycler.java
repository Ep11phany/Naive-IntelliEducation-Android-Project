package com.example.ksandroidplayerdemo;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;
import java.util.List;


//暂时无用，将类写在activity中以调用activity中的变量
public class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

    private List<Item> mItemList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

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
        holder.Name.setOnClickListener(new View.OnClickListener() {//对加载的子项注册监听事件
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Item item = mItemList.get(position);
                Toast.makeText(view.getContext(), "You clicked view " + item.getName(),Toast.LENGTH_SHORT).show();
            }
        });
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
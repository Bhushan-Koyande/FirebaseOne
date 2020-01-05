package com.example.firebaseone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {
    private ArrayList<ListItem> itemArrayList;

    public static class ListItemViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;

        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.itemImageView);
            textView1=itemView.findViewById(R.id.itemName);
            textView2=itemView.findViewById(R.id.itemPhone);
            textView3=itemView.findViewById(R.id.itemAddress);
        }
    }

    public ListItemAdapter(ArrayList<ListItem> items){
        itemArrayList=items;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        ListItemViewHolder listItemViewHolder=new ListItemViewHolder(v);
        return listItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        ListItem currentItem=itemArrayList.get(position);
        holder.imageView.setImageResource(currentItem.getmImageResource());
        holder.textView1.setText(currentItem.getmName());
        holder.textView2.setText(currentItem.getmPhone());
        holder.textView3.setText(currentItem.getmAddress());
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }
}

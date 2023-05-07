package com.example.appandroid.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appandroid.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListProductHistoryAdapter extends RecyclerView.Adapter<ListProductHistoryAdapter.ListProductHistoryViewHolder> {

    private List<ProductHistory> listProductHistory;

    public ListProductHistoryAdapter(List<ProductHistory> listProductHistory) {
        this.listProductHistory = listProductHistory;

    }

    @NonNull
    @Override
    public ListProductHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_history, parent, false);
        return new ListProductHistoryAdapter.ListProductHistoryViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull ListProductHistoryViewHolder holder, int position) {
        ProductHistory productHistory = listProductHistory.get(position);
        if(productHistory == null) {
            return;
        }

        holder.nameProductHistory.setText(productHistory.getName());
        holder.quantityProductHistory.setText(Integer.toString(productHistory.getQuantity()));
        holder.dateProductHistory.setText(productHistory.getDate());
        Picasso.get().load(productHistory.getPath()).into(holder.imageProductHistory);
    }

    @Override
    public int getItemCount() {
        if(listProductHistory != null) {
            return  listProductHistory.size();
        }
        return 0;
    }

    public  class ListProductHistoryViewHolder extends RecyclerView.ViewHolder {

        ImageView imageProductHistory;
        TextView quantityProductHistory, dateProductHistory, nameProductHistory;

        public ListProductHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProductHistory = (ImageView) itemView.findViewById(R.id.imageProductHistory);
            quantityProductHistory = (TextView) itemView.findViewById(R.id.quantityProductHistory);
            nameProductHistory = (TextView) itemView.findViewById(R.id.nameProductHistory);
            dateProductHistory = (TextView) itemView.findViewById(R.id.dateProductHistory);
        }
    }
}

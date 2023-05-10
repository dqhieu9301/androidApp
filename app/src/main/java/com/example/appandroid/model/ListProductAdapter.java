package com.example.appandroid.model;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appandroid.DetailsFoodActivity;
import com.example.appandroid.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ListProductViewHolder> {
    private List<Product> productList ;
    private Context context;

    public ListProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }
    @NonNull
    @Override
    public ListProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);
        return new ListProductViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull ListProductViewHolder holder, int position) {
        Product product = productList.get(position);
        if(product == null) {
            return;
        }
//        int id = context.getResources().getIdentifier(product.getPath(), "drawable", context.getPackageName());
        holder.name.setText(product.getName().toString());
        holder.cost.setText(product.getCost() + " VND");
        Picasso.get().load(product.getPath()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, DetailsFoodActivity.class);
                intent.putExtra("idProduct", product.getId() + "");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(productList != null) {
            return  productList.size();
        }
        return 0;
    }

    public class ListProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView name, cost;
        public ListProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.product_image);
            name = (TextView) itemView.findViewById(R.id.product_name);
            cost = (TextView) itemView.findViewById(R.id.product_cost);
         }
    }
}

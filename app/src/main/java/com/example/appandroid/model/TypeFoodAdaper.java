package com.example.appandroid.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appandroid.ListProductActivity;
import com.example.appandroid.R;

import java.util.List;

public class TypeFoodAdaper extends RecyclerView.Adapter<TypeFoodAdaper.TypeFoodViewHolder>  {
    private List<TypeFood> listTypeFood ;
    private Context context;
    public TypeFoodAdaper(List<TypeFood> listTypeOfFood) {
        this.listTypeFood = listTypeOfFood;
    }
    @NonNull
    @Override
    public TypeFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_food, parent, false);
        return new TypeFoodViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull TypeFoodViewHolder holder, int position) {
        TypeFood typeFood = listTypeFood.get(position);
        if(typeFood == null) {
            return;
        }
        holder.imageView.setImageResource(typeFood.getImg());
        holder.textView.setText(typeFood.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context = view.getContext();
                TextView textView = (TextView) view.findViewById(R.id.food_name);
                String name = textView.getText().toString();
                Intent intent = new Intent(context, ListProductActivity.class);
                intent.putExtra("name", name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listTypeFood != null) {
            return  listTypeFood.size();
        }
        return 0;
    }

    public class TypeFoodViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;
        public TypeFoodViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.food_image);
            textView = (TextView) itemView.findViewById(R.id.food_name);
        }
    }
}

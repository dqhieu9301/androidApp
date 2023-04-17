package com.example.appandroid.model;

import android.content.Context;
import android.os.CpuUsageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appandroid.R;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<Product> pList;
    private Context context;
    private FoodItemLístens foodItemLístens;
    public  FoodAdapter(List<Product> pList,Context context)
    {
        this.pList = pList;
        this.context = context;
    }

    public void setFoodItemLístens(FoodItemLístens foodItemLístens) {
        this.foodItemLístens = foodItemLístens;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_in_cart,parent, false);

        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Product product = pList.get(position);
        if (product == null){
            return;
        }
        int id = context.getResources().getIdentifier(product.getPathImage(),"drawable", context.getPackageName());
        holder.img.setImageResource(id);
        holder.name.setText(product.getNameProduct().toString());
        holder.gia.setText("Giá: " + product.getCost() + " VND");
        holder.soluong.setText("Số lượng: " +product.getQuantity());
    }

    @Override
    public int getItemCount() {
        if(pList != null)
            return pList.size();
        return 0;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView img;
        private TextView name, gia, soluong;
        public FoodViewHolder(@NonNull View itemView){
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            gia = itemView.findViewById(R.id.gia);
            soluong = itemView.findViewById(R.id.soluong);
            itemView.setOnClickListener(this);
        }
        public  void  onClick(View view){
            if(foodItemLístens!=null){
                foodItemLístens.onItemClick(view,getAdapterPosition());
            }
        }
    }
    public  interface FoodItemLístens{
        public void onItemClick(View view, int position);
    }
}

package com.example.appandroid.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CpuUsageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appandroid.DetailProductActivity;
import com.example.appandroid.ListProductActivity;
import com.example.appandroid.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<ItemCart> pList;
    private Context context;
    private ItemCart itemCart;
    private int ps;
    public FoodAdapter(List<ItemCart> pList,Context context) {
        this.pList = pList;
        this.context = context;
    }


    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_in_cart, parent, false);

        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        itemCart = pList.get(position);
        ps = pList.indexOf(itemCart);
        if (itemCart == null) {
            return;
        }

        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        System.out.println(token);
        Picasso.get().load(itemCart.getProduct().getPath()).into(holder.img);
        holder.name.setText(itemCart.getProduct().getName());
        holder.cost.setText("Giá: " + itemCart.getProduct().getCost() + " VND");
        holder.quantity.setText(itemCart.getQuantity() + "");
        holder.total.setText("Tổng: " + itemCart.getProduct().getCost() * itemCart.getQuantity() + " VND");
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                TextView textView = (TextView) view.findViewById(R.id.name);
                String name = textView.getText().toString();
                Intent intent = new Intent(context, DetailProductActivity.class);
                intent.putExtra("name", name);
                context.startActivity(intent);
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                TextView textView = (TextView) view.findViewById(R.id.name);
                String name = textView.getText().toString();
                Intent intent = new Intent(context, DetailProductActivity.class);
                intent.putExtra("product name", itemCart.getProduct().getName());
                context.startActivity(intent);
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCart.setQuantity(itemCart.getQuantity() + 1);
                pList.set(ps, itemCart);
                OkHttpClient client = new OkHttpClient();
                String url = "http://192.168.0.107:3000/api/cart-product/update/" + itemCart.getId();
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");
                Request request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).build();
                try (Response response = client.newCall(request).execute()) {
                    String responseData = response.body().string();
                    // Process the response data here
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemCart.getQuantity() > 1) {
                    itemCart.setQuantity(itemCart.getQuantity() - 1);
                    pList.set(ps, itemCart);
                    OkHttpClient client = new OkHttpClient();
                    String url = "http://192.168.0.107:3000/api/cart-product/update/" + itemCart.getId();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String token = sharedPreferences.getString("token", "");
                    Request request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).build();
                    try (Response response = client.newCall(request).execute()) {
                        String responseData = response.body().string();
                        // Process the response data here
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pList.remove(itemCart);
                OkHttpClient client = new OkHttpClient();
                String url = "http://192.168.0.107:3000/api/cart-product/delete/" + itemCart.getId();
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");
                Request request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).build();
                try (Response response = client.newCall(request).execute()) {
                    String responseData = response.body().string();
                    // Process the response data here
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (pList != null)
            return pList.size();
        return 0;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        private ImageView img, sub, add;
        private TextView name, cost, quantity, total;

        private Button delete;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            cost = itemView.findViewById(R.id.cost);
            quantity = itemView.findViewById(R.id.quantity);
            total = itemView.findViewById(R.id.total);
            sub = itemView.findViewById(R.id.sub);
            add = itemView.findViewById(R.id.add);
            delete = itemView.findViewById(R.id.delete);
        }

    }

}

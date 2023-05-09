package com.example.appandroid.model;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appandroid.DetailProductActivity;
import com.example.appandroid.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//
public class FoodBoughtAdapter extends RecyclerView.Adapter<FoodBoughtAdapter.FoodBoughtViewHolder>{

    private List<ItemCart> pList;

    private Context context;
    private ItemCart itemCart;

    public FoodBoughtAdapter(List<ItemCart> pList, Context context) {
        this.pList = pList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodBoughtAdapter.FoodBoughtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_in_history_bought, parent, false);

        return new FoodBoughtAdapter.FoodBoughtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodBoughtViewHolder holder, int position) {
        itemCart = pList.get(position);
        int ps;
        ps = pList.indexOf(itemCart);

        if (itemCart == null) {
            return;
        }
        Picasso.get().load(itemCart.getProduct().getPath()).into(holder.img);
        holder.name.setText(itemCart.getProduct().getName());
        holder.cost.setText("Giá: " + itemCart.getProduct().getCost() + " VND");
        holder.quantity.setText(itemCart.getQuantity() + "");
        holder.total.setText("Tổng: " + itemCart.getProduct().getCost() * itemCart.getQuantity() + " VND");

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = itemCart.getProduct().getName();
                Intent intent = new Intent(context, DetailProductActivity.class);
                intent.putExtra("idProduct", itemCart.getProduct().getId());
                context.startActivity(intent);
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailProductActivity.class);
                intent.putExtra("idProduct", itemCart.getProduct().getId());
                context.startActivity(intent);
            }
        });

        holder.add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCart = pList.get(ps);
                add_food_to_cart(itemCart);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (pList != null)
            return pList.size();
        return 0;
    }
    public void add_food_to_cart(ItemCart itemCart){
        String str_body = "{\"id\": "+ itemCart.getProduct().getId() +"\"quantity\": 1 }";
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, str_body);
        OkHttpClient client = new OkHttpClient();
        String url = "http://20.205.137.244/api/cart-product/add_to_cart";
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Request request = new Request.Builder().url(url).post(body).addHeader("Authorization", "Bearer " + token).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                }
        });

    }
    public class FoodBoughtViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView name, cost, quantity, total;

        private Button add_to_cart;
        public FoodBoughtViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            cost = itemView.findViewById(R.id.cost);
            quantity = itemView.findViewById(R.id.quantity);
            total = itemView.findViewById(R.id.total);
            add_to_cart = itemView.findViewById(R.id.add_to_cart);
        }

    }
}

package com.example.appandroid.model;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appandroid.DetailProductActivity;
import com.example.appandroid.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//
public class FoodBoughtAdapter extends RecyclerView.Adapter<FoodBoughtAdapter.FoodBoughtViewHolder> {

    private List<ProductHistory> pList;

    private Context context;
    private ProductHistory productHistory;

    String token;

    public FoodBoughtAdapter(List<ProductHistory> pList, Context context, String token) {
        this.pList = pList;
        this.context = context;
        this.token = token;
    }

    @NonNull
    @Override
    public FoodBoughtAdapter.FoodBoughtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_in_history_bought, parent, false);
        return new FoodBoughtAdapter.FoodBoughtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodBoughtViewHolder holder, int position) {
        productHistory = pList.get(position);
        int ps;
        ps = pList.indexOf(productHistory);

        if (productHistory == null) {
            return;
        }
        Picasso.get().load(productHistory.getPath()).into(holder.img);
        holder.name.setText(productHistory.getName());
        holder.cost.setText("Giá: " + productHistory.getCost() + " VND");
        holder.quantity.setText("Số lượng: " + productHistory.getQuantity() + "");
        holder.total.setText("Tổng: " + productHistory.getCost() * productHistory.getQuantity() + " VND");
        String date_update = productHistory.getUpdated_at().toString().split("T")[0];
        holder.date.setText("Ngày: " + date_update);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetailProductActivity.class);
                intent.putExtra("idProduct", productHistory.getId());
                context.startActivity(intent);
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailProductActivity.class);
                intent.putExtra("idProduct", productHistory.getId());
                context.startActivity(intent);
            }
        });

        holder.add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productHistory = pList.get(ps);
                add_food_to_cart(productHistory, token, v);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (pList != null)
            return pList.size();
        return 0;
    }

    public void add_food_to_cart(ProductHistory productHistory, String token, View view) {
        String str_body = "{\"id\": " + productHistory.getId() + ",\"quantity\": 1}";
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, str_body);
        OkHttpClient client = new OkHttpClient();
        String url = "http://20.205.137.244/api/cart-product/add-to-cart";
        Request request = new Request.Builder().url(url).post(body).addHeader("Authorization", "Bearer " + token).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Context context1 = view.getContext();
                ((Activity) context1).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(context, "Thêm vào giỏ thành công", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }

    public class FoodBoughtViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView name, cost, quantity, total, date;

        private Button add_to_cart;

        public FoodBoughtViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgProductHistory);
            name = itemView.findViewById(R.id.nameProductHistory);
            cost = itemView.findViewById(R.id.costProductHistory);
            quantity = itemView.findViewById(R.id.quantityProductHistory);
            total = itemView.findViewById(R.id.totalProductHistory);
            add_to_cart = itemView.findViewById(R.id.add_to_cart);
            date = itemView.findViewById(R.id.dateProductHistory);
        }

    }
}

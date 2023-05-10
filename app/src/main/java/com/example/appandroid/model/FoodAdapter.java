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
import com.example.appandroid.ListProductActivity;
import com.example.appandroid.R;
import com.example.appandroid.ui.cart.CartFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<ItemCart> pList;
    private Context context;
    private ItemCart itemCart;
    private Fragment fragment;

    public FoodAdapter(List<ItemCart> pList, Context context, Fragment fragment) {
        this.pList = pList;
        this.context = context;
        this.fragment = fragment;
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
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCart = pList.get(ps);
                itemCart.setQuantity(itemCart.getQuantity() + 1);
                pList.set(ps, itemCart);
                holder.total.setText("Tổng: " + itemCart.getProduct().getCost() * itemCart.getQuantity() + " VND");
                holder.quantity.setText(itemCart.getQuantity() + "");
                showTotal_payment();
                update_quatity(itemCart.getId(), itemCart.getQuantity());
            }
        });
        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemCart.getQuantity() > 1) {
                    itemCart = pList.get(ps);
                    itemCart.setQuantity(itemCart.getQuantity() - 1);
                    pList.set(ps, itemCart);
                    holder.total.setText("Tổng: " + itemCart.getProduct().getCost() * itemCart.getQuantity() + " VND");
                    holder.quantity.setText(itemCart.getQuantity() + "");
                    showTotal_payment();
                    update_quatity(itemCart.getId(), itemCart.getQuantity());

                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pList.remove(itemCart);
                OkHttpClient client = new OkHttpClient();
                String url = "http://20.205.137.244/api/cart-product/delete/" + itemCart.getId();
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");
                Request request = new Request.Builder().url(url).delete().addHeader("Authorization", "Bearer " + token).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        Gson gson = new Gson();

                        Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
                        }.getType());
                        String message = (String) map.get("message");
                        if (message.equals("delete success")) {
                            Context context1 = v.getContext();
                            ((Activity) context1).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Xóa đối tượng khỏi danh sách của Adapter
                                    pList.remove(ps);
                                    notifyItemRemoved(ps);
                                    notifyItemRangeChanged(ps, pList.size());
                                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    showTotal_payment();
                                }
                            });

                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if (pList != null)
            return pList.size();
        return 0;
    }
    public void showTotal_payment() {
        int total = 0;
        if (pList.size() > 0) {
            for (ItemCart itemCart : pList) {
                total += itemCart.getQuantity() * itemCart.getProduct().getCost();
            }
        }
        TextView textView = fragment.getView().findViewById(R.id.total_payment);
        textView.setText(total + " VND");
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

    public void update_quatity(int idItemCart, int quanity_food) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://20.205.137.244/api/cart-product/update/" + idItemCart;
        String quantity = "{\"quantity\": " + quanity_food + "}";

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, quantity);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Request request = new Request.Builder().url(url).put(body).addHeader("Authorization", "Bearer " + token).build();

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


}

package com.example.appandroid.ui.cart;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appandroid.MainActivity;
import com.example.appandroid.R;
import com.example.appandroid.model.FoodAdapter;
import com.example.appandroid.model.ItemCart;
import com.example.appandroid.model.ListProductAdapter;
import com.example.appandroid.model.Product;
import com.google.gson.Gson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView1, recyclerView2;
    private FoodAdapter foodAdapter;
    private ListProductAdapter productAdapter;
    private TextView total_payment;
    private ImageView imageback;
    private Button buttonBuy;
    private List<ItemCart> listItemCarts;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView1 = view.findViewById(R.id.recycleView_listFoods);
        total_payment = view.findViewById(R.id.total_payment);
        imageback = view.findViewById(R.id.imageback);
        buttonBuy = view.findViewById(R.id.buttonBuy);

        int total = 0;
        getRecylerView();
        for (ItemCart itemCart : listItemCarts) {
            total += itemCart.getQuantity() + itemCart.getProduct().getCost();
        }
        total_payment.setText("Tổng thanh toán\n" + total + " VND");
        Context context = getContext();
        recyclerView2 = view.findViewById(R.id.recycleView_listProducts);
        productAdapter = new ListProductAdapter(getList(), context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView2.setLayoutManager(gridLayoutManager);
        recyclerView2.setAdapter(productAdapter);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getContext();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBuy();
            }
        });
    }

    private void getBuy() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://192.168.121.1:3000/api/cart-product/Buy";
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            String responseData = response.body().string();
            // Process the response data here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getRecylerView() {

        OkHttpClient client = new OkHttpClient();
        Moshi moshi = new Moshi.Builder().build();
        Type productsType = Types.newParameterizedType(List.class, ItemCart.class);
        JsonAdapter<List<ItemCart>> jsonAdapter = moshi.adapter(productsType);

        String url = "http://192.168.121.1:3000/api/cart-product/getAll";

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Request request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject reader = new JSONObject(json);
                    String item_srting = reader.getString("listProduct");
                    listItemCarts = jsonAdapter.fromJson(item_srting);
                    actionListItemCart(listItemCarts);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }


        });
    }


    private void actionListItemCart(List<ItemCart> ItemCarts) {
        Context context = getContext();
        foodAdapter = new FoodAdapter(ItemCarts, context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        recyclerView1.setLayoutManager(gridLayoutManager);
        recyclerView1.setAdapter(foodAdapter);
    }

    private List<Product> getList() {
        List<Product> list = new ArrayList<>();
        Context context = getContext();
        String path_image = "http://res.cloudinary.com/doe8iuzbo/image/upload/v1682134318/ejo8rvmlf0zf9m1qp7mt.png";
        list.add(new Product(1, "pizza", "bánh", path_image, 200000, 1));
        path_image = "http://res.cloudinary.com/doe8iuzbo/image/upload/v1682134334/ngu68ubwokfe9rmoqwxv.png";
        list.add(new Product(2, "hamburger", "bánh", path_image, 30000, 1));
        path_image = "http://res.cloudinary.com/doe8iuzbo/image/upload/v1682135561/kz2vqbixxcvhatvmsgba.png";
        list.add(new Product(3, "fried_chicken", "bánh", path_image, 50000, 1));
        return list;
    }

}
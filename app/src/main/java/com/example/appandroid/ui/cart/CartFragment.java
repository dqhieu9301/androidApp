package com.example.appandroid.ui.cart;


import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appandroid.LoginActivity;
import com.example.appandroid.MainActivity;
import com.example.appandroid.R;
import com.example.appandroid.model.FoodAdapter;
import com.example.appandroid.model.ItemCart;
import com.example.appandroid.model.ListProductAdapter;
import com.example.appandroid.model.Product;
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
    private Context context;
    private RecyclerView recyclerView1, recyclerView2;
    private FoodAdapter foodAdapter;
    private ListProductAdapter productAdapter;
    public TextView total_payment;
    private Button buttonBuy;
    private List<ItemCart> listItemCarts;
    private JsonAdapter<List<ItemCart>> jsonAdapter;

    private List<Product> products;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        total_payment = view.findViewById(R.id.total_payment);
        buttonBuy = view.findViewById(R.id.buttonBuy);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);
        if (token == null) {
            ConstraintLayout containerCart = view.findViewById(R.id.containerCart);
            containerCart.setVisibility(View.GONE);
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
        else{
            getRecylerView(view, token);
        }
        get_recomendable_product(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBuy();
            }
        });
    }

    private void getBuy() {
        String str_body = "{\"id\": "+ 1 +",\"quantity\": " + 1 + "}";
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, str_body);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        OkHttpClient client = new OkHttpClient();
        String url = "http://20.205.137.244/api/cart-product/buy-products-in-cart";

        Request request = new Request.Builder().url(url).post(body).addHeader("Authorization", "Bearer " + token).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Toast.makeText(context.getApplicationContext(), "Đặt hàng thành công!!!", Toast.LENGTH_SHORT).show();


            }
        });

    }
    private void getRecylerView(View view, String token) {
        recyclerView1 = view.findViewById(R.id.recycleView_listFoods);
        OkHttpClient client = new OkHttpClient();
        Moshi moshi = new Moshi.Builder().build();
        Type productsType = Types.newParameterizedType(List.class, ItemCart.class);
        jsonAdapter = moshi.adapter(productsType);

        String url = "http://20.205.137.244/api/cart-product/getAll";
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
                    String item_string = reader.getString("listProduct");
                    listItemCarts = jsonAdapter.fromJson(item_string);
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            actionListItemCart(listItemCarts);
                        }
                    });

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }


        });
    }


    private void actionListItemCart(List<ItemCart> ItemCarts) {
        int total = 0;

        for (ItemCart itemCart : ItemCarts) {
            total += itemCart.getQuantity() * itemCart.getProduct().getCost();
        }
        total_payment.setText(total + " VND");
        Context context = getContext();
        foodAdapter = new FoodAdapter(ItemCarts, context, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        recyclerView1.setLayoutManager(gridLayoutManager);
        recyclerView1.setAdapter(foodAdapter);
    }

    private void get_recomendable_product(View view) {
        recyclerView2 = view.findViewById(R.id.recycleView_listProducts);
        OkHttpClient client = new OkHttpClient();
        Moshi moshi = new Moshi.Builder().build();
        Type productsType = Types.newParameterizedType(List.class, Product.class);
        JsonAdapter<List<Product>> jsonProductAdapter = moshi.adapter(productsType);
        String url = "http://20.205.137.244/api/cart-product/random-product";
        Request request = new Request.Builder().url(url).build();
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
                    String products_string = reader.getString("listProductRandom");
                    products = jsonProductAdapter.fromJson(products_string);
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            actionProducts(products);
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }


        });
    }

    void showCustomDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.requires_login_modal);

        final Button btnChangeLogin = dialog.findViewById(R.id.btnChangeLogin);
        btnChangeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }
    private void actionProducts(List<Product> products) {
        productAdapter = new ListProductAdapter(products, context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView2.setLayoutManager(gridLayoutManager);
        recyclerView2.setAdapter(productAdapter);
    }
}
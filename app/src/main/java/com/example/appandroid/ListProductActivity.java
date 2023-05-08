package com.example.appandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListProductAdapter listProductAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        recyclerView = findViewById(R.id.recycleView_listProduct);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        Context context = getApplicationContext();
        OkHttpClient client = new OkHttpClient();
        Moshi moshi = new Moshi.Builder().build();
        Type productsType = Types.newParameterizedType(List.class, Product.class);
        JsonAdapter<List<Product>> jsonAdapter = moshi.adapter(productsType);

        String url = "http://http://20.205.137.244/api/product/get-list-product?type=" + name;
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
                    String listProduct = reader.getString("listProduct");
                    final List<Product> products = jsonAdapter.fromJson(listProduct);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            actionListProduct(products);
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

    private void actionListProduct(List<Product> productList) {
        Context context = getApplicationContext();
        listProductAdapter = new ListProductAdapter(productList, context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(listProductAdapter);
    }
}
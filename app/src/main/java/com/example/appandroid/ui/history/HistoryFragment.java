package com.example.appandroid.ui.history;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appandroid.R;
import com.example.appandroid.model.ListProductAdapter;
import com.example.appandroid.model.ListProductHistoryAdapter;
import com.example.appandroid.model.Product;
import com.example.appandroid.model.ProductHistory;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerViewlistProductHistory;
    private Context context;

    private ListProductHistoryAdapter listProductHistoryAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerViewlistProductHistory = (RecyclerView) view.findViewById(R.id.recycleView_listProductHistory);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        sharedPreferences.edit().clear().commit();
        String token = sharedPreferences.getString("token", null);

        String url = "http://20.205.137.244/api/cart-product/get-list-product-history";
        OkHttpClient client = new OkHttpClient();
        Moshi moshi = new Moshi.Builder().build();
        Type productsType = Types.newParameterizedType(List.class, ProductHistory.class);
        JsonAdapter<List<ProductHistory>> jsonAdapter = moshi.adapter(productsType);
        Request request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject reader = new JSONObject(json);
                    String listProductHistory = reader.getString("listProductHistory");
                    final List<ProductHistory> productHistoryList = jsonAdapter.fromJson(listProductHistory);
                    System.out.println(productHistoryList);
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            actionListProduct(productHistoryList);
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return view;
    }

    private void actionListProduct(List<ProductHistory> productList) {
        System.out.println(1);
        listProductHistoryAdapter = new ListProductHistoryAdapter(productList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,1);
        recyclerViewlistProductHistory.setLayoutManager(gridLayoutManager);
        recyclerViewlistProductHistory.setAdapter(listProductHistoryAdapter);
    }
}

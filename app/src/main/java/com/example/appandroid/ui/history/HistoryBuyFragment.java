package com.example.appandroid.ui.history;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appandroid.LoginActivity;
import com.example.appandroid.R;
import com.example.appandroid.model.FoodAdapter;
import com.example.appandroid.model.FoodBoughtAdapter;
import com.example.appandroid.model.ItemCart;
import com.example.appandroid.model.ListProductAdapter;
import com.example.appandroid.model.Product;
import com.example.appandroid.model.ProductHistory;
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

public class HistoryBuyFragment extends Fragment {
    private Context context;
    private RecyclerView recyclerView1, recyclerView2;
    private JsonAdapter<List<ProductHistory>> jsonAdapter;

    private ListProductAdapter productAdapter;

    private FoodBoughtAdapter foodBoughtAdapter;
    private List<ProductHistory> productsHistory;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_buy, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        sharedPreferences.edit().clear().commit();
        String token = sharedPreferences.getString("token", null);
        System.out.println(token);
        if (token == null) {
            showCustomDialog();
        }
        recyclerView1 = view.findViewById(R.id.recycleView_listFoods);
        getRecylerView();
//        recyclerView2 = view.findViewById(R.id.recycleView_listProducts);
//        productAdapter = new ListProductAdapter(getList(), context);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
//        recyclerView2.setLayoutManager(gridLayoutManager);
//        recyclerView2.setAdapter(productAdapter);
        return view;
    }

    private void getRecylerView() {

        OkHttpClient client = new OkHttpClient();
        Moshi moshi = new Moshi.Builder().build();
        Type productsType = Types.newParameterizedType(List.class, ProductHistory.class);
        jsonAdapter = moshi.adapter(productsType);

        String url = "http://20.205.137.244/api/cart-product/get-list-product-history";

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
                    String products_string = reader.getString("listProductHistory");
                    productsHistory = jsonAdapter.fromJson(products_string);
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            actionListItemCart(productsHistory);
                        }
                    });

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }


        });
    }

    private void actionListItemCart(List<ProductHistory> productsHistory) {
        Context context = getContext();
        foodBoughtAdapter = new FoodBoughtAdapter(productsHistory, context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        recyclerView1.setLayoutManager(gridLayoutManager);
        recyclerView1.setAdapter(foodBoughtAdapter);
    }
    private List<Product> getList() {
        List<Product> list = new ArrayList<>();
        String path_image = "http://res.cloudinary.com/doe8iuzbo/image/upload/v1682134318/ejo8rvmlf0zf9m1qp7mt.png";
        list.add(new Product(1, "pizza", "bánh", path_image, 200000, 1));
        path_image = "http://res.cloudinary.com/doe8iuzbo/image/upload/v1682134334/ngu68ubwokfe9rmoqwxv.png";
        list.add(new Product(2, "hamburger", "bánh", path_image, 30000, 1));
        path_image = "http://res.cloudinary.com/doe8iuzbo/image/upload/v1682135561/kz2vqbixxcvhatvmsgba.png";
        list.add(new Product(3, "fried_chicken", "bánh", path_image, 50000, 1));
        return list;
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
}

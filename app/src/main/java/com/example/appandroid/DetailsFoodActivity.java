package com.example.appandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

public class DetailsFoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_food);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        String idProduct = intent.getStringExtra("idProduct");

        OkHttpClient client = new OkHttpClient();
        Moshi moshi = new Moshi.Builder().build();
        Type productsType = Types.newParameterizedType(List.class, Product.class);
        JsonAdapter<List<Product>> jsonAdapter = moshi.adapter(productsType);

        String url = "http://20.205.137.244/api/product/get-detail-product/" + idProduct;
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
                    final List<Product>[] products = new List[]{new ArrayList<>()};
                    JSONObject reader = new JSONObject(json);
                    String item = reader.getString("product");



                    JSONObject reader1 = new JSONObject(item);
                    String id = reader1.getString("id");
                    String name = reader1.getString("name");
                    String type = reader1.getString("type");
                    String cost = reader1.getString("cost");
                    String path = reader1.getString("path");

                    TextView tenmon = (TextView)findViewById(R.id.textView);
                    tenmon.setText(name);

                    TextView giamon = (TextView)findViewById(R.id.textView5);
                    giamon.setText(cost);

                    ImageView imageView = findViewById(R.id.imageView2);
                    String imageName = path;
                    int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
//                    System.out.println(imageName);
                    imageView.setImageResource(resId);

//                    products = jsonAdapter.fromJson(item);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            System.out.println(products[0]);
//                            System.out.println(item);
//                            System.out.println(id);
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }
}
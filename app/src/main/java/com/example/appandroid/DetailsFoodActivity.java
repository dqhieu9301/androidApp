package com.example.appandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appandroid.model.Product;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.picasso.Picasso;

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

public class DetailsFoodActivity extends AppCompatActivity {

    String name;
    String cost;
    String path;
    String imageName;
    String idProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_food);
        setTitle("Chi tiết món ăn");

//        getSupportActionBar().hide();

        Intent intent = getIntent();
        idProduct = intent.getStringExtra("idProduct");

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
                    name = reader1.getString("name");
                    String type = reader1.getString("type");
                    cost = reader1.getString("cost");
                    String describe = reader1.getString("describe");
                    String quantity = reader1.getString("quantity");
                    path = reader1.getString("path");

                    TextView tenmon = (TextView)findViewById(R.id.textView);
                    TextView giamon = (TextView)findViewById(R.id.textView5);
                    TextView mota = (TextView)findViewById(R.id.textView2);

                    ImageView imageView = findViewById(R.id.imageView2);
                    imageName = path;
                    System.out.println(imageName);

                    Handler handler = new Handler(Looper.getMainLooper());
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            tenmon.setText(name.toUpperCase());
                            giamon.setText(cost + " VNĐ");
                            mota.setText(describe);
//                            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
//                            imageView.setImageResource(resId);
                            Picasso.get().load(imageName).into(imageView);
                        }
                    };
                    handler.post(runnable);

//                    products = jsonAdapter.fromJson(item);

                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appears_gradually);
                    View newLayout = findViewById(R.id.button3);
                    newLayout.startAnimation(anim);

                    Button addcartbtn = (Button) findViewById(R.id.button3);

                    addcartbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showConfirmDialog();
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            System.out.println(item);
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

    void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.confirm_food, null);

//        Button btnChangeLogin = dialogView.findViewById(R.id.btnChangeLogin);
        TextView tenmondialog = (TextView)dialogView.findViewById(R.id.textView7);
        TextView giatiendialog = (TextView)dialogView.findViewById(R.id.textView8);
        TextView soluongdialog = (TextView)dialogView.findViewById(R.id.textView9);
        ImageView imageView = dialogView.findViewById(R.id.imageView9);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setContentView(R.layout.confirm_food);

        Handler handler = new Handler(Looper.getMainLooper());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tenmondialog.setText(name.toUpperCase());
                giatiendialog.setText(cost + " VNĐ");
                Picasso.get().load(imageName).into(imageView);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        Button increasebtn = dialogView.findViewById(R.id.button5);
        Button decreasebtn = dialogView.findViewById(R.id.button6);
        increasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int intsoluongdialog = Integer.parseInt(soluongdialog.getText().toString()) + 1;
                int intgiatiendialog = Integer.parseInt(cost) * intsoluongdialog;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        soluongdialog.setText(Integer.toString(intsoluongdialog));
                        giatiendialog.setText(Integer.toString(intgiatiendialog) + " VNĐ");
                    }
                });
            }
        });
        decreasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int intsoluongdialog = Integer.parseInt(soluongdialog.getText().toString()) - 1;
                int intgiatiendialog = Integer.parseInt(cost) * intsoluongdialog;

                if (intsoluongdialog <= 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailsFoodActivity.this);
                    builder.setTitle("CẢNH BÁO");
                    builder.setMessage("Dữ liệu không hợp lệ!!!");
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Khi người dùng bấm nút OK, đóng dialog
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            soluongdialog.setText(Integer.toString(intsoluongdialog));
                            giatiendialog.setText(Integer.toString(intgiatiendialog) + " VNĐ");
                        }
                    });
                }
            }
        });

        Button closeConfirm = dialogView.findViewById(R.id.button2);
        closeConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button acceptConfirm = dialogView.findViewById(R.id.button4);
        acceptConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String token = sharedPreferences.getString("token", null);

                if(token == null) {
                    Intent intent = new Intent(DetailsFoodActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    TextView totalitemsdialog = dialogView.findViewById(R.id.textView9);
                    String totalitems = totalitemsdialog.getText().toString();
                    int idfood = Integer.parseInt(idProduct);
                    int quantity = Integer.parseInt(totalitems);

                    add_food_to_cart(idfood, quantity);

                    dialog.dismiss();
                }

            }
        });
    }

    public void add_food_to_cart(int idfood, int quantity){
        String str_body = "{\"id\": "+ idfood +",\"quantity\": " + quantity + "}";
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, str_body);
        OkHttpClient client = new OkHttpClient();
        String url = "http://20.205.137.244/api/cart-product/add-to-cart";
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Thêm vào giỏ hàng thành công!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
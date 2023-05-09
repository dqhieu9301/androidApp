package com.example.appandroid;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.example.appandroid.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterActivity extends AppCompatActivity {
    private ImageView imageView,imagedangnhap;
    private Button btn ;
    private EditText edtten,edtdiachi,edtsdt,edttaikhoan,edtmatkhau,edtxacnhanmatkhau;
    private JsonElement User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imageView = findViewById(R.id.imageView);
        imageView.setAlpha(98);
        imagedangnhap = findViewById(R.id.imageDN);
        btn = (Button) findViewById(R.id.buttonDK);
        edtten= findViewById(R.id.editTextfullname);
        edtdiachi= findViewById(R.id.editTextdiachi);
        edtsdt= findViewById(R.id.editTextsdt);
        edttaikhoan= findViewById(R.id.editTexttaikhoan);
        edtmatkhau= findViewById(R.id.editTextmatkhau);
        edtxacnhanmatkhau = findViewById(R.id.editTextxacnhanmatkhau);
        imagedangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten = edtten.getText().toString();
                System.out.println(ten);
                String diachi = edtdiachi.getText().toString();
                String sdt = edtsdt.getText().toString();
                String taikhoan =edttaikhoan.getText().toString();
                String matkhau = edtmatkhau.getText().toString();
                String xacnhanmatkhau = edtxacnhanmatkhau.getText().toString();
                User user = new User(ten,diachi,sdt,taikhoan,matkhau);
                Gson gson = new Gson();
                String json = gson.toJson(User);
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, json);
//                String url = "http://20.205.137.244/api/auth/register";
//                Request request = new Request.Builder().url(url).post(body).build();
                register(ten,diachi,sdt,taikhoan,matkhau, xacnhanmatkhau);

//                client.newCall(request).enqueue(new Callback() {
//
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        System.out.println(e);
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        String json = response.body().string();
//                        System.out.println(json);
//
//                    }
//                });
            }
        });
    }

    private void register(String ten, String diachi, String sdt, String taikhoan, String matkhau,String xacnhanmatkhau) {
        String url = "http://20.205.137.244/api/auth/register";
        OkHttpClient client = new OkHttpClient();
        // Tạo đối tượng JSON chứa thông tin đăng ký
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put("username", taikhoan);
            requestJson.put("password", matkhau);
            requestJson.put("fullname", ten);
            requestJson.put("address", diachi);
            requestJson.put("phone", sdt);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Tạo đối tượng RequestBody chứa dữ liệu yêu cầu
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestJson.toString());
        // Tạo đối tượng Request chứa URL và Header thông tin yêu cầu
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        // Gửi yêu cầu đến server và sử lý kết quả trả về
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
                // Xử lý lỗi kết nối
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject responseJson = new JSONObject(responseBody);
//                        boolean success = responseJson.getBoolean("success");
//                        if (success) {
//                            String token = responseJson.getString("token");
//                            // Lưu token vào SharedPreferences
//                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString("token", token);
//                            editor.apply();
//                            // Chuyển về màn hình đăng nhập
//                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            String message = responseJson.getString("message");
//                            // Xử lý thông báo đăng ký thất bại
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý lỗi phản hồi yêu cầu đăng ký
                    System.out.println("error");
                    showError(response.message());
                }
            }
            private void showError(String message) {
                System.out.println(message);
            }
        });
    }
}
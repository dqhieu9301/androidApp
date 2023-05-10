package com.example.appandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.appandroid.model.Product;
import com.example.appandroid.model.RegisterUser;
import com.example.appandroid.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterActivity extends AppCompatActivity {

    EditText editTextfullname, editTextdiachi, editTextsdt, editTexttaikhoan, editTextmatkhau, editTextxacnhanmatkhau;
    Button buttonDK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        inital();

        System.out.println(1);
        buttonDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(editTextfullname.getText())) {
                    editTextfullname.setError("Không được bỏ trống");
                    return;
                }

                if(TextUtils.isEmpty(editTextdiachi.getText())) {
                    editTextdiachi.setError("Không được bỏ trống");
                    return;
                }

                if(TextUtils.isEmpty(editTextsdt.getText())) {
                    editTextsdt.setError("Không được bỏ trống");
                    return;
                }

                if(TextUtils.isEmpty(editTexttaikhoan.getText())) {
                    editTexttaikhoan.setError("Không được bỏ trống");
                    return;
                }

                if(TextUtils.isEmpty(editTextmatkhau.getText())) {
                    editTextmatkhau.setError("Không được bỏ trống");
                    return;
                }

                if(TextUtils.isEmpty(editTextxacnhanmatkhau.getText())) {
                    editTextxacnhanmatkhau.setError("Không được bỏ trống");
                    return;
                }

                String regex = "\\+84\\d{9}";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(editTextsdt.getText().toString());
                if(!matcher.matches()) {
                    editTextsdt.setError("Phải là định dạng +84 và 9 số ");
                    return;
                }

                if(!editTextmatkhau.getText().toString().equals(editTextxacnhanmatkhau.getText().toString())) {
                    editTextxacnhanmatkhau.setError("Mật khẩu không khớp");
                    return;
                }

                String url = "http://20.205.137.244/api/auth/register";
                Context context = getApplicationContext();
                OkHttpClient client = new OkHttpClient();
                RegisterUser registerUser = new RegisterUser(editTexttaikhoan.getText().toString(), editTextmatkhau.getText().toString(), editTextfullname.getText().toString(), editTextdiachi.getText().toString(), editTextsdt.getText().toString());
                Gson gson = new Gson();
                String json = gson.toJson(registerUser);
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, json);
                Request request = new Request.Builder().url(url).post(body).build();
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
                            String statusCode = reader.getString("statusCode");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(Integer.parseInt(statusCode) == 400) {
                                        editTexttaikhoan.setError("Tài khoản đã tồn tại");
                                        return;
                                    } else if(Integer.parseInt(statusCode) == 201) {
                                        CharSequence text = "Đăng ký thành công";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });

            }
        });
    }

    public void inital() {
        editTextfullname = findViewById(R.id.editTextfullname);
        editTextdiachi = findViewById(R.id.editTextdiachi);
        editTextsdt = findViewById(R.id.editTextsdt);
        editTexttaikhoan = findViewById(R.id.editTexttaikhoan);
        editTextmatkhau = findViewById(R.id.editTextmatkhau);
        editTextxacnhanmatkhau = findViewById(R.id.editTextxacnhanmatkhau);
        buttonDK = findViewById(R.id.buttonDK);

    }
}
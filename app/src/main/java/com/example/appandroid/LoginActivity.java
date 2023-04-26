package com.example.appandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appandroid.model.Account;
import com.google.gson.Gson;

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

public class LoginActivity extends AppCompatActivity {

    ImageView imageView;
    Button btn;
    TextView textView;
    EditText editTextTextPersonName2,editTextTextPassword2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imageView = findViewById(R.id.imageView4);
        imageView.setAlpha(98);
        btn = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView5);
        editTextTextPersonName2 = findViewById(R.id.editTextTextPersonName2);
        editTextTextPassword2 = findViewById(R.id.editTextTextPassword2);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();
                String username = editTextTextPersonName2.getText().toString();
                String pass = editTextTextPassword2.getText().toString();
//                Moshi moshi = new Moshi.Builder().build();
//                User user = (User) Types.newParameterizedType(User.class, User.class);
//                Type productsType = null;
//                JsonAdapter<User> jsonAdapter = moshi.adapter(productsType);
                Account account = new Account(pass,username);
                Gson gson = new Gson();
                String json = gson.toJson(account);
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, json);
                String url = "http://192.168.27.1:3000/api/auth/login";
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
                            String token = reader.getString("accessToken");
                            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token", token);
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }

                });

            }

        });
    }
}
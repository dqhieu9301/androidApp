package com.example.appandroid.ui.user;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.appandroid.LoginActivity;
import com.example.appandroid.MainActivity;
import com.example.appandroid.R;
import com.example.appandroid.RegisterActivity;
import com.example.appandroid.model.Product;
import com.example.appandroid.model.User;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ResourceBundle;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserFragment extends Fragment {

    private EditText edtTen;
    private EditText edtDiachi;
    private EditText edtsdt;
    private Button btndn;
    private Button btndx;
    private Button btnfalse;
    private Button btntrue;
    private TextView logout, fullnameTextView;
    private Context context;
    private ImageView imageView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        imageView = view.findViewById(R.id.imagelogout);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        edtTen = view.findViewById(R.id.editTextTextPersonName7);
        edtDiachi = view.findViewById(R.id.editTextTextPersonName6);
        edtsdt = view.findViewById(R.id.editTextTextPersonName5);
        fullnameTextView = view.findViewById(R.id.textView7);

        LinearLayout containerInforUser = view.findViewById(R.id.containerInforUser);


        if(token == null) {
            containerInforUser.setVisibility(View.GONE);
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }else {
            logout = view.findViewById(R.id.logout);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedPreferences.edit().clear().commit();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });

            String url = "http://20.205.137.244/api/auth/infor-user";
            OkHttpClient client = new OkHttpClient();
            Moshi moshi = new Moshi.Builder().build();
            Type inforUser = Types.newParameterizedType(List.class, User.class);
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
                        String inforUserString = reader.getString("inforUser");
                        JSONObject jsonObject = new JSONObject(inforUserString);
                        String address = jsonObject.getString("address");
                        String phone = jsonObject.getString("phone");
                        String fullname = jsonObject.getString("fullname");
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                edtsdt.setText(phone);
                                edtDiachi.setText(address);
                                edtTen.setText(fullname);
                                fullnameTextView.setText(fullname);

                            }
                        });

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }


            });
        }
        return view;
    }
    void logOut() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
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
}
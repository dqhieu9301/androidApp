package com.example.appandroid.ui.user;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
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


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.appandroid.LoginActivity;
import com.example.appandroid.MainActivity;
import com.example.appandroid.R;

import java.util.ResourceBundle;

public class UserFragment extends Fragment {

    private EditText edtTen;
    private EditText edtDiachi;
    private EditText edtsdt;
    private Button btndn;
    private Button btndx;
    private Button btnfalse;
    private Button btntrue;
    private ImageView imageView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        imageView = view.findViewById(R.id.imagelogout);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        sharedPreferences.edit().clear().commit();
        String token = sharedPreferences.getString("token", null);
        if(token == null) {
            showCustomDialog();
        }else {
            logOut();
            if (token == null){
                showCustomDialog();
            }
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
package com.example.appandroid.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.appandroid.LoginActivity;
import com.example.appandroid.MainActivity;
import com.example.appandroid.R;

public class UserFragment extends Fragment {

    private EditText edtTen;
    private EditText edtDiachi;
    private EditText edtsdt;
    private Button btndn;
    private Button btndx;
    private  Button btnfalse;
    private  Button btntrue;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        sharedPreferences.edit().clear().commit();
        String token = sharedPreferences.getString("token", null);
        System.out.println(token);
        if(token == null) {
            showCustomDialog();
        }

        return view;
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
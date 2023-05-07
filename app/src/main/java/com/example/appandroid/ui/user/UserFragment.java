package com.example.appandroid.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
        return view;
    }


}
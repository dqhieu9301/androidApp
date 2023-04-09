package com.example.appandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.appandroid.model.ListProductAdapter;
import com.example.appandroid.model.Product;
import com.example.appandroid.model.TypeFoodAdaper;
import com.example.appandroid.sqlite.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class ListProductActivity extends AppCompatActivity {

    SQLiteHelper sqLiteHelper;
    private RecyclerView recyclerView;
    private ListProductAdapter listProductAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        recyclerView = findViewById(R.id.recycleView_listProduct);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        Context context = getApplicationContext();
        sqLiteHelper = new SQLiteHelper((context));
        List<Product> productList = sqLiteHelper.getListProductByType(name);
        actionListProduct(productList);
    }

    private void actionListProduct(List<Product> productList) {
        Context context = getApplicationContext();
        listProductAdapter = new ListProductAdapter(productList, context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(listProductAdapter);
    }
}
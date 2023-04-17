package com.example.appandroid.ui.cart;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appandroid.R;
import com.example.appandroid.model.FoodAdapter;
import com.example.appandroid.model.ListProductAdapter;
import com.example.appandroid.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements FoodAdapter.FoodItemLístens{
    private RecyclerView recyclerView1, recyclerView2;
    private FoodAdapter foodAdapter;
    private ListProductAdapter productAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView1 = view.findViewById(R.id.recycleView_listFood);
        recyclerView2 = view.findViewById(R.id.recycleView_listProduct);
        Context context = getContext();
        foodAdapter = new FoodAdapter(getList(),context);
        productAdapter = new ListProductAdapter(getList(),context);

        foodAdapter.setFoodItemLístens(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        recyclerView2.setLayoutManager(gridLayoutManager);
        recyclerView2.setAdapter(productAdapter);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(context, 1);
        recyclerView1.setLayoutManager(gridLayoutManager1);
        recyclerView1.setAdapter(foodAdapter);
        return view;
    }
    private List<Product> getList(){
        List<Product> list = new ArrayList<>();
        Context context = getContext();
        String path_image = "@drawable/pizza";
        list.add(new Product("pizza","bánh",path_image,200000,1));
        path_image = "@drawable/hamburger";
        list.add(new Product("hamburger","bánh",path_image,30000,1));
        path_image = "@drawable/fried_chicken";
        list.add(new Product("fried_chicken","bánh",path_image,50000,1));
        return list;
    }

    @Override
    public void onItemClick(View view, int position) {
        Product product = getList().get(position);
        Toast.makeText(getContext(),product.getNameProduct(),Toast.LENGTH_SHORT).show();
    }
}
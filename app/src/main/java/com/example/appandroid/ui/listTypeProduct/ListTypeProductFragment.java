package com.example.appandroid.ui.listTypeProduct;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.appandroid.R;
import com.example.appandroid.model.TypeFood;
import com.example.appandroid.model.TypeFoodAdaper;

import java.util.ArrayList;
import java.util.List;

public class ListTypeProductFragment extends Fragment {
    private TypeFoodAdaper typeOfFoodAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_type_product, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView_listTypeProduct);
        actionListTypeOfFood();
        return view;
    }

    private void actionListTypeOfFood() {
        typeOfFoodAdapter = new TypeFoodAdaper(getList());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(typeOfFoodAdapter);
    }
    private List<TypeFood> getList() {
        List<TypeFood> typeFoodList = new ArrayList<>();
        typeFoodList.add(new TypeFood(R.drawable.do_uong, "Nước"));
        typeFoodList.add(new TypeFood(R.drawable.cac_loai_banh, "Bánh"));
        typeFoodList.add(new TypeFood(R.drawable.cac_loai_sup, "Súp"));
        typeFoodList.add(new TypeFood(R.drawable.do_anh_nhanh, "Đồ ăn nhanh"));
        return  typeFoodList;
    };
}

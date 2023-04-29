package com.example.appandroid.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.appandroid.ListProductActivity;
import com.example.appandroid.R;
import com.example.appandroid.model.TypeFood;
import com.example.appandroid.model.TypeFoodAdaper;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private TypeFoodAdaper typeOfFoodAdapter;
    private RecyclerView recyclerView;
    private EditText editText;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageSlider imageSlider = (ImageSlider) view.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slide_cac_loai_banh, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_cac_loai_do_an_nhanh, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_cac_loai_sup, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_nuoc_uong, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView_listTypeFood);
        actionListTypeOfFood();

        editText = (EditText) view.findViewById(R.id.searchProduct);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    System.out.println(1);
                    Intent intent = new Intent(getActivity(), ListProductActivity.class);
                    String valueEditText = editText.getText().toString();
                    intent.putExtra("name", valueEditText);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
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
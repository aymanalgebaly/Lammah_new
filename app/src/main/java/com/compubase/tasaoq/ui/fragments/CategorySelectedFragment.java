package com.compubase.tasaoq.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.compubase.tasaoq.R;
import com.compubase.tasaoq.adapter.CategorySelectedAdapter;
import com.compubase.tasaoq.data.API;
import com.compubase.tasaoq.helper.RetrofitClient;
import com.compubase.tasaoq.helper.TinyDB;
import com.compubase.tasaoq.model.CategoryModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategorySelectedFragment extends Fragment {


    @BindView(R.id.rcv_category_selected)
    RecyclerView rcvCategorySelected;
    Unbinder unbinder;
    @BindView(R.id.img_category)
    ImageView imgCategory;
    @BindView(R.id.txt_value)
    TextView txtValue;
    private CategoryModel categoryModel;
    private CategorySelectedAdapter categorySelectedAdapter;
    private List<CategoryModel> productsModelArrayList = new ArrayList<>();
    private TinyDB tinyDB;
    private String title;
    private String cat;

    public CategorySelectedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_selected, container, false);
        unbinder = ButterKnife.bind(this, view);
        tinyDB = new TinyDB(getActivity());

        int img = tinyDB.getInt("img");
        title = tinyDB.getString("title");

        Glide.with(Objects.requireNonNull(getActivity())).load(img).into(imgCategory);
        txtValue.setText(title);

        setupRecycler();
        fetchDataTopRated();

        return view;
    }

    private void setupRecycler() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rcvCategorySelected.setLayoutManager(linearLayoutManager);
        categorySelectedAdapter = new CategorySelectedAdapter(getActivity());
        rcvCategorySelected.setAdapter(categorySelectedAdapter);
        categorySelectedAdapter.notifyDataSetChanged();

    }

    private void fetchDataTopRated() {

        productsModelArrayList.clear();

        Call<ResponseBody> call2 = RetrofitClient.getInstant().create(API.class).showCategory("1",title);

        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                try {
                    assert response.body() != null;
                    List<CategoryModel> categoryModels = Arrays.asList(gson.fromJson(response.body().string(),
                            CategoryModel[].class));

                    if (response.isSuccessful()) {

                        for (int j = 0; j < categoryModels.size(); j++) {

                            categoryModel = new CategoryModel();

                            categoryModel.setId(categoryModels.get(j).getIdAdmin());
                            categoryModel.setCategory(categoryModels.get(j).getCategory());
                            categoryModel.setDes(categoryModels.get(j).getDes());
                            categoryModel.setImg1(categoryModels.get(j).getImg1());
//                            categoryModel.setImg2(categoryModels.get(j).getImg2());
//                            categoryModel.setImg3(categoryModels.get(j).getImg3());
                            categoryModel.setTitle(categoryModels.get(j).getTitle());
                            categoryModel.setNumberRate(categoryModels.get(j).getNumberRate());
                            categoryModel.setPrice(categoryModels.get(j).getPrice());
                            categoryModel.setPriceDiscount(categoryModels.get(j).getPriceDiscount());
                            categoryModel.setRate(categoryModels.get(j).getRate());

                            productsModelArrayList.add(categoryModel);
                        }
                        categorySelectedAdapter = new CategorySelectedAdapter(productsModelArrayList);
                        rcvCategorySelected.setAdapter(categorySelectedAdapter);
                        categorySelectedAdapter.notifyDataSetChanged();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

                Log.i("onFailure: ", t.getMessage());
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

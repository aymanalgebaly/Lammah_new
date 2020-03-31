package com.compubase.tasaoq.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.compubase.tasaoq.R;
import com.compubase.tasaoq.adapter.CategoriesAdapter;
import com.compubase.tasaoq.adapter.CategoriesNavigationAdapter;
import com.compubase.tasaoq.data.API;
import com.compubase.tasaoq.helper.RetrofitClient;
import com.compubase.tasaoq.model.CategoriesModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class CategoriesFragment extends Fragment {


    @BindView(R.id.rcv_category_fragment)
    RecyclerView rcvCategoryFragment;
    Unbinder unbinder;
    private CategoriesNavigationAdapter categoriesAdapter;
    private ArrayList<CategoriesModel> categoriesModelArrayList = new ArrayList<>();
    private String img;

    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        unbinder = ButterKnife.bind(this, view);

        setupRecycler();
        fetchData();
        return view;
    }

    private void setupRecycler() {

        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(),2);
        rcvCategoryFragment.setLayoutManager(linearLayoutManager);
        categoriesAdapter = new CategoriesNavigationAdapter(getActivity());
        rcvCategoryFragment.setAdapter(categoriesAdapter);
        categoriesAdapter.notifyDataSetChanged();

    }

    public void fetchData() {


        RetrofitClient.getInstant().create(API.class).selecte_all_category().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                try {
                    assert response.body() != null;
                    List<CategoriesModel> categoriesModels =
                            Arrays.asList(gson.fromJson(response.body().string(), CategoriesModel[].class));

                    if (response.isSuccessful()){

                        for (int i = 0; i <categoriesModels.size() ; i++) {


                            img = categoriesModels.get(i).getImg();
                            CategoriesModel categoriesModel = new CategoriesModel();

                            categoriesModel.setName(categoriesModels.get(i).getName());
                            categoriesModel.setImg(categoriesModels.get(i).getImg());
                            categoriesModel.setId(categoriesModels.get(i).getId());
                            categoriesModel.setDateregister(categoriesModels.get(i).getDateregister());

                            categoriesModelArrayList.add(categoriesModel);
                        }

                        categoriesAdapter.setAdapter(categoriesModelArrayList);
                        categoriesAdapter.notifyDataSetChanged();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

//        ArrayList<CategoriesModel> categoriesModels = new ArrayList<>();
//
//        int[] image = new int[]{R.drawable.kago,R.drawable.loz,R.drawable.fozdo2,R.drawable.leb,
//                R.drawable.dwar_elshams,R.drawable.sodany,R.drawable.amar_eldein};
//
//        String[]title = new String[]{"كاجو","لوز","فزدق","لب","زيوت","سودانى","قمر الدين"};
//
//        for (int i = 0; i <image.length ; i++) {
//
//            categoriesModels.add(new CategoriesModel(title[i],image[i]));
//        }
//        categoriesAdapter.setAdapter(categoriesModels);
//        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

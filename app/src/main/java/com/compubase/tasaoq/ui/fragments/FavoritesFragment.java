package com.compubase.tasaoq.ui.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.compubase.tasaoq.R;
import com.compubase.tasaoq.adapter.CategoriesAdapter;
import com.compubase.tasaoq.adapter.FavAdapter;
import com.compubase.tasaoq.data.API;
import com.compubase.tasaoq.helper.RetrofitClient;
import com.compubase.tasaoq.model.FavModel;
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
public class FavoritesFragment extends Fragment {


    @BindView(R.id.rcv_fav)
    RecyclerView rcvFav;
    Unbinder unbinder;
    private CategoriesAdapter categoriesAdapter;
    private SharedPreferences preferences;
    private String id;
    private FavModel favModel;
    private ArrayList<FavModel> favModelArrayList = new ArrayList<>();
    private FavAdapter favAdapter;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        unbinder = ButterKnife.bind(this, view);

        preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        id = preferences.getString("id", "");

//        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();

        setupRecyclerTopRated();
        fetchDataTopRated();
        return view;
    }

    private void fetchDataTopRated() {

        favModelArrayList.clear();

        Call<ResponseBody> call2 = RetrofitClient.getInstant().create(API.class).selectFave(id);

        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                try {
                    assert response.body() != null;
                    List<FavModel> favModels = Arrays.asList(gson.fromJson(response.body().string(), FavModel[].class));

                    if (response.isSuccessful()){

                        for (int j = 0; j <favModels.size() ; j++) {

                            favModel = new FavModel();

                            favModel.setId(favModels.get(j).getIdAdmin());
                            favModel.setCategory(favModels.get(j).getCategory());
                            favModel.setDes(favModels.get(j).getDes());
                            favModel.setImg1(favModels.get(j).getImg1());
                            favModel.setImg2(favModels.get(j).getImg2());
                            favModel.setImg3(favModels.get(j).getImg3());
                            favModel.setTitle(favModels.get(j).getTitle());
                            favModel.setId(favModels.get(j).getId());
                            favModel.setIdUser(favModels.get(j).getIdUser());
                            favModel.setId1(favModels.get(j).getId1());
                            favModel.setNumberRate(favModels.get(j).getNumberRate());
                            favModel.setPrice(favModels.get(j).getPrice());
                            favModel.setPriceDiscount(favModels.get(j).getPriceDiscount());
                            favModel.setRate(favModels.get(j).getRate());

                            favModelArrayList.add(favModel);

                            Log.i("onResponse",favModelArrayList.toString());

                        }
                        favAdapter = new FavAdapter(favModelArrayList);
                        rcvFav.setAdapter(favAdapter);
                        favAdapter.notifyDataSetChanged();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

                Log.i("onFailure: ",t.getMessage());
            }
        });
    }


    private void setupRecyclerTopRated() {

        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(),2);
        rcvFav.setLayoutManager(linearLayoutManager);
//        topRatedAdapter = new TopRatedAdapter(getActivity());
//        rcvTopRated.setAdapter(topRatedAdapter);
//        topRatedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

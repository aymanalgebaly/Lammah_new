package com.compubase.tasaoq.ui.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.compubase.tasaoq.R;
import com.compubase.tasaoq.adapter.OrdersAdapter;
import com.compubase.tasaoq.adapter.TopRatedAdapter;
import com.compubase.tasaoq.data.API;
import com.compubase.tasaoq.helper.RetrofitClient;
import com.compubase.tasaoq.model.LammahOrdersModel;
import com.compubase.tasaoq.model.ProductsModel;
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
public class HistoryFragment extends Fragment {


    @BindView(R.id.rcv_history)
    RecyclerView rcvHistory;
    Unbinder unbinder;
    private SharedPreferences preferences;
    private String id;
    private OrdersAdapter ordersAdapter;
    private List<LammahOrdersModel> lammahOrdersModels = new ArrayList<>();

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);
        preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE);
        id = preferences.getString("id", "");

        setupRecycler();
        fetchDataTopRated();
        return view;
    }
    private void setupRecycler() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(false);
        rcvHistory.setLayoutManager(linearLayoutManager);

    }
    private void fetchDataTopRated() {

        lammahOrdersModels.clear();

        Call<ResponseBody> call2 = RetrofitClient.getInstant().create(API.class).MyOrders(id);

        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                try {
                    assert response.body() != null;
                    lammahOrdersModels =
                            Arrays.asList(gson.fromJson(response.body().string(), LammahOrdersModel[].class));

//                    if (response.isSuccessful()){
//
//                        for (int j = 0; j <productsModels.size() ; j++) {
//
//                            productsModelsList = new ProductsModel();
//
////                            productsModelsList.setId(productsModels.get(j).getIdAdmin());
//                            productsModelsList.setCategory(productsModels.get(j).getCategory());
//                            productsModelsList.setDes(productsModels.get(j).getDes());
//                            productsModelsList.setImg1(productsModels.get(j).getImg1());
//                            productsModelsList.setImg2(productsModels.get(j).getImg2());
//                            productsModelsList.setImg3(productsModels.get(j).getImg3());
//                            productsModelsList.setTitle(productsModels.get(j).getTitle());
//                            productsModelsList.setId(productsModels.get(j).getId());
//                            productsModelsList.setNumberRate(productsModels.get(j).getNumberRate());
//                            productsModelsList.setPrice(productsModels.get(j).getPrice());
//                            productsModelsList.setPriceDiscount(productsModels.get(j).getPriceDiscount());
//                            productsModelsList.setRate(productsModels.get(j).getRate());
//
//                            id_pro = productsModels.get(j).getId();
//
//                            Log.i( "onResponse", String.valueOf(productsModels.get(j).getId()));
//
//                            productsModelArrayList.add(productsModelsList);
//                            Log.i("onResponseHome",productsModelArrayList.toString());
//                        }
                        ordersAdapter = new OrdersAdapter(lammahOrdersModels);
                        rcvHistory.setAdapter(ordersAdapter);
                        ordersAdapter.notifyDataSetChanged();

                    } catch (IOException e1) {


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

                Log.i("onFailure: ",t.getMessage());
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

package com.compubase.tasaoq.ui.fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.compubase.tasaoq.R;
import com.compubase.tasaoq.adapter.OrdersAdapter;
import com.compubase.tasaoq.adapter.ViewOrderAdapter;
import com.compubase.tasaoq.data.API;
import com.compubase.tasaoq.helper.RetrofitClient;
import com.compubase.tasaoq.model.LammahOrdersModel;
import com.compubase.tasaoq.model.ProductOfOrdersModel;
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
public class ViewOrderFragment extends Fragment {


    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.txt_id)
    TextView txtId;
    @BindView(R.id.txt_price)
    TextView txtPrice;
    @BindView(R.id.rcv_viewOrder)
    RecyclerView rcvViewOrder;
    Unbinder unbinder;
    private Integer idOrder;
    private String datee,price;
    private List<ProductOfOrdersModel> productOfOrdersModels = new ArrayList<>();
    private ViewOrderAdapter viewOrderAdapter;

    public ViewOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null){
            LammahOrdersModel id_order = getArguments().getParcelable("id_order");
            assert id_order != null;
            idOrder = id_order.getId();
            datee = id_order.getDatee();
            price = id_order.getTotlePrice();
        }

        txtDate.setText(datee);
        txtId.setText(String.valueOf(idOrder));
        txtPrice.setText(price);
        setupRecycler();
        fetchDataTopRated();
        return view;
    }
    private void setupRecycler() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(false);
        rcvViewOrder.setLayoutManager(linearLayoutManager);

    }
    private void fetchDataTopRated() {

        productOfOrdersModels.clear();

        Call<ResponseBody> call2 = RetrofitClient.getInstant().create(API.class).MyOrderDetails(String.valueOf(idOrder));

        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                try {
                    assert response.body() != null;
                    productOfOrdersModels =
                            Arrays.asList(gson.fromJson(response.body().string(), ProductOfOrdersModel[].class));

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
                    viewOrderAdapter = new ViewOrderAdapter(productOfOrdersModels);
                    rcvViewOrder.setAdapter(viewOrderAdapter);
                    viewOrderAdapter.notifyDataSetChanged();

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

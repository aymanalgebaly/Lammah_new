package com.compubase.tasaoq.ui.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.compubase.tasaoq.R;
import com.compubase.tasaoq.adapter.CartAdapter;
import com.compubase.tasaoq.data.API;
import com.compubase.tasaoq.helper.RequestHandler;
import com.compubase.tasaoq.helper.RetrofitClient;
import com.compubase.tasaoq.model.ProductsModel;
import com.compubase.tasaoq.ui.activities.HomeActivity;
import com.paytabs.paytabs_sdk.payment.ui.activities.PayTabActivity;
import com.paytabs.paytabs_sdk.utils.PaymentParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
//import io.realm.Realm;
//import io.realm.RealmResults;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {


    @BindView(R.id.rcv_cart)
    RecyclerView rcvCart;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.total_price_cart)
    TextView totalPriceCart;
    @BindView(R.id.lin_cart)
    LinearLayout linCart;
    Unbinder unbinder;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.rel_prog)
    RelativeLayout relProg;
    @BindView(R.id.ed_address)
    EditText edAddress;

    private int totalPricee = 0;
    private SharedPreferences preferences;

    Realm realm;

    private List<ProductsModel> productsModels;

    private ArrayList<ProductsModel> productsModelList = new ArrayList<>();

    private CartAdapter cartAdapter;

    private String[] list;
    private String id;
    private Integer iid;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        unbinder = ButterKnife.bind(this, view);

        Realm.init(Objects.requireNonNull(getContext()));
        realm = Realm.getDefaultInstance();

        preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE);
        id = preferences.getString("id", "");

        setupRecycler();
        showData();
//        refreshData();
        return view;
    }

    private void setupRecycler() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(false);
        rcvCart.setLayoutManager(linearLayoutManager);

    }

    @SuppressLint("SetTextI18n")
    private void showData() {

        productsModelList.clear();

        RealmResults<ProductsModel> all = realm.where(ProductsModel.class).findAll();
        for (int i = 0; i < all.size(); i++) {

            ProductsModel productsModel = new ProductsModel();

            productsModel.setTitle(Objects.requireNonNull(all.get(i)).getTitle());
            productsModel.setDes(Objects.requireNonNull(all.get(i)).getDes());
            productsModel.setImg1(Objects.requireNonNull(all.get(i)).getImg1());
            productsModel.setPrice(Objects.requireNonNull(all.get(i)).getPrice());
            productsModel.setId(Objects.requireNonNull(all.get(i)).getId());
            productsModel.setIsfav(Objects.requireNonNull(all.get(i)).getIsfav());

            assert all.get(i) != null;
            iid = all.get(i).getId();

            productsModelList.add(productsModel);


            if (all.get(i) != null) {
                String price = all.get(i).getPrice();
                int pr = Integer.parseInt(price);
                totalPricee = totalPricee + pr;

                totalPriceCart.setText(String.valueOf(totalPricee));
            }
        }
//        realm.commitTransaction();
        cartAdapter = new CartAdapter(productsModelList,totalPriceCart);
//        double totalPrice = cartAdapter.getTotalPrice();
//        totalPriceCart.setText(String.valueOf(totalPrice));
        rcvCart.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        relProg.setVisibility(View.VISIBLE);
//        Payment();
//        functionVolly();
        HomeActivity homeActivity = (HomeActivity) getActivity();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data",productsModelList);
        bundle.putInt("price",totalPricee);
        ConfirmFragment confirmFragment = new ConfirmFragment();
        confirmFragment.setArguments(bundle);
        Objects.requireNonNull(homeActivity).displaySelectedFragmentWithBack(confirmFragment);
        relProg.setVisibility(View.GONE);
//        functionVolly();
    }
    private void Payment() {

        Intent in = new Intent(getActivity(), PayTabActivity.class);
        in.putExtra(PaymentParams.MERCHANT_EMAIL, "sportive2050@gmail.com"); //this a demo account for testing the sdk
        in.putExtra(PaymentParams.SECRET_KEY,"t5eeZqLRUSZ2lTCzYhruLiKShpuKFwb9CqnCR9tL2tOomrXlIoPuHznYZSIEoUO1kcDbl7XoBMMXdKjW98qQHNPGGxl5s96MmJYH");//Add your Secret Key Here
        in.putExtra(PaymentParams.LANGUAGE,PaymentParams.ENGLISH);
        in.putExtra(PaymentParams.TRANSACTION_TITLE, "Payment");
        in.putExtra(PaymentParams.AMOUNT,5.0);

        in.putExtra(PaymentParams.CURRENCY_CODE, "SAR");
        in.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, "00966515435133");
        in.putExtra(PaymentParams.CUSTOMER_EMAIL, "customer-email@example.com");
        in.putExtra(PaymentParams.ORDER_ID, "123456");
        in.putExtra(PaymentParams.PRODUCT_NAME, "Product 1, Product 2");

//Billing Address
        in.putExtra(PaymentParams.ADDRESS_BILLING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_BILLING, "jeddah");
        in.putExtra(PaymentParams.STATE_BILLING, "jeddah");
        in.putExtra(PaymentParams.COUNTRY_BILLING, "SAR");
        in.putExtra(PaymentParams.POSTAL_CODE_BILLING, "21577"); //Put Country Phone code if Postal code not available '00973'

//Shipping Address
        in.putExtra(PaymentParams.ADDRESS_SHIPPING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_SHIPPING, "jeddah");
        in.putExtra(PaymentParams.STATE_SHIPPING, "jeddah");
        in.putExtra(PaymentParams.COUNTRY_SHIPPING, "SAR");
        in.putExtra(PaymentParams.POSTAL_CODE_SHIPPING, "21577"); //Put Country Phone code if Postal code not available '00973'

//Payment Page Style
        in.putExtra(PaymentParams.PAY_BUTTON_COLOR, "#c27637");
//        in.putExtra(PaymentParams.THEME, PaymentParams.THEME_LIGHT);

//Tokenization
        in.putExtra(PaymentParams.IS_TOKENIZATION, true);
        startActivityForResult(in, PaymentParams.PAYMENT_REQUEST_CODE);

    }


    private void functionVolly() {

//            String idUser = ""; // Shof B2a Btgebo Mnen
        String address = edAddress.getText().toString(); // Shof B2a Btgebo Mnen
//            String totalPrice = ""; // Shof B2a Btgebo Mnen


        StringBuilder GET_JSON_DATA_HTTP_URL =
                new StringBuilder("http://fastini.alosboiya.com.sa/store_app.asmx/insert_orders?id_user=" +
                id + "&address=" + address + "&totle_price=" + totalPricee);

//        String sample = "http://fastini.alosboiya.com.sa/store_app.asmx/insert_orders?" +
//                "id_user=" + id + "&address=" + address + "&totle_price=" + totalPrice +
//                "&id_product=" + iid + "&id_product=" + iid + "&id_product=" + iid;


        for (int i = 0; i <= productsModelList.size() - 1; i++) {
            GET_JSON_DATA_HTTP_URL.append("&id_product=").append(String.valueOf(productsModelList.get(i).getId()));
        }

        Toast.makeText(getActivity(), GET_JSON_DATA_HTTP_URL, Toast.LENGTH_SHORT).show();

        Log.i("functionVolly", GET_JSON_DATA_HTTP_URL.toString());
//        Log.i("functionVolly", sample);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_JSON_DATA_HTTP_URL.toString(),

                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                            Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        if (response.equals("True")) {
                            HomeActivity homeActivity = (HomeActivity) getActivity();
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("data",productsModelList);
                            ConfirmFragment confirmFragment = new ConfirmFragment();
                            confirmFragment.setArguments(bundle);
                            Objects.requireNonNull(homeActivity).displaySelectedFragmentWithBack(confirmFragment);
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }

        });

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }

    private void confirmOrder() {

        String[] title = new String[]{"كاجو", "لوز"};

        List<String> stringList = new ArrayList<>();

        stringList.add("one");
        stringList.add("two");

        Retrofit retrofit = RetrofitClient.getInstant();
        API api = retrofit.create(API.class);
        Call<ResponseBody> responseBodyCall = api.insertOrders("2", "hhhh", "5000", stringList);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(getActivity(), "order insert", Toast.LENGTH_SHORT).show();
                    Log.i("onResponse", String.valueOf(response));

                    try {
                        assert response.body() != null;
                        String string = response.body().string();
                        if (string.equals("True")) {
                            Toast.makeText(getActivity(), "response order insert", Toast.LENGTH_SHORT).show();
                            Log.i("onResponses", String.valueOf(response));

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PaymentParams.PAYMENT_REQUEST_CODE) {
            Log.e("Tag", data.getStringExtra(PaymentParams.RESPONSE_CODE));
            Log.e("Tag", data.getStringExtra(PaymentParams.TRANSACTION_ID));
            if (data.hasExtra(PaymentParams.TOKEN) && !data.getStringExtra(PaymentParams.TOKEN).isEmpty()) {
                Log.e("Tag", data.getStringExtra(PaymentParams.TOKEN));
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_EMAIL));
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD));
            }
        }
    }

}

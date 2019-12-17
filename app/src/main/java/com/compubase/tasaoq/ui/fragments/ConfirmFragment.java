package com.compubase.tasaoq.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.compubase.tasaoq.R;
import com.compubase.tasaoq.adapter.CartAdapter;
import com.compubase.tasaoq.helper.RequestHandler;
import com.compubase.tasaoq.model.ProductsModel;
import com.compubase.tasaoq.ui.activities.HomeActivity;
import com.google.gson.Gson;
import com.paytabs.paytabs_sdk.payment.ui.activities.PayTabActivity;
import com.paytabs.paytabs_sdk.utils.PaymentParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;
//import io.realm.Realm;
//import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmFragment extends Fragment {


    @BindView(R.id.rcv_confirm)
    RecyclerView rcvConfirm;
    @BindView(R.id.txt_total_value)
    TextView txtTotalValue;
    @BindView(R.id.ed_address)
    EditText edAddress;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    Unbinder unbinder;
    @BindView(R.id.search_radio_btn)
    RadioButton searchRadioBtn;
    @BindView(R.id.adv_search_radio_btn)
    RadioButton advSearchRadioBtn;
    @BindView(R.id.lin_one)
    RadioGroup linOne;

    private Realm realm;

    private List<ProductsModel> productsModelList = new ArrayList<>();
    private CartAdapter cartAdapter;
    private ArrayList<ProductsModel> data = new ArrayList<>();
    private int price;
    private SharedPreferences preferences;
    private String id;

    public ConfirmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm, container, false);
        unbinder = ButterKnife.bind(this, view);

        preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE);
        id = preferences.getString("id", "");

        if (getArguments() != null) {
            data = getArguments().getParcelableArrayList("data");
            price = getArguments().getInt("price");
        }

        txtTotalValue.setText(String.valueOf(price));
        Realm.init(Objects.requireNonNull(getContext()));
        realm = Realm.getDefaultInstance();



        setupRecycler();
        showData();
        return view;
    }

    private void setupRecycler() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(false);
        rcvConfirm.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

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

            productsModelList.add(productsModel);

            Log.i("showData", new Gson().toJson(data));


        }
//        realm.commitTransaction();
        cartAdapter = new CartAdapter(productsModelList);
        rcvConfirm.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

    }


    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        functionVolly();

    }
    private void Payment() {

        Intent in = new Intent(getActivity(), PayTabActivity.class);
        in.putExtra(PaymentParams.MERCHANT_EMAIL, "sportive2050@gmail.com"); //this a demo account for testing the sdk
        in.putExtra(PaymentParams.SECRET_KEY,"t5eeZqLRUSZ2lTCzYhruLiKShpuKFwb9CqnCR9tL2tOomrXlIoPuHznYZSIEoUO1kcDbl7XoBMMXdKjW98qQHNPGGxl5s96MmJYH");//Add your Secret Key Here
        in.putExtra(PaymentParams.LANGUAGE,PaymentParams.ENGLISH);
        in.putExtra(PaymentParams.TRANSACTION_TITLE, "Payment");
        in.putExtra(PaymentParams.AMOUNT,Double.valueOf(price));

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
                        id + "&address=" + address + "&totle_price=" + price);

//        String sample = "http://fastini.alosboiya.com.sa/store_app.asmx/insert_orders?" +
//                "id_user=" + id + "&address=" + address + "&totle_price=" + totalPrice +
//                "&id_product=" + iid + "&id_product=" + iid + "&id_product=" + iid;


        for (int i = 0; i <= productsModelList.size() - 1; i++) {
            GET_JSON_DATA_HTTP_URL.append("&id_product=").append(String.valueOf(productsModelList.get(i).getId()));
        }

//        Toast.makeText(getActivity(), GET_JSON_DATA_HTTP_URL, Toast.LENGTH_SHORT).show();

        Log.i("functionVolly", GET_JSON_DATA_HTTP_URL.toString());
//        Log.i("functionVolly", sample);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_JSON_DATA_HTTP_URL.toString(),

                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                            Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        if (response.equals("True")) {
                            if (searchRadioBtn.isChecked()){
                                Toast.makeText(getContext(), "تم ارسال الطلب", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getContext(),HomeActivity.class));
                                Objects.requireNonNull(getActivity()).finish();
                            }else if (advSearchRadioBtn.isChecked()){
                                Payment();
                            }else {
                                Toast.makeText(getContext(), "من فضلك اختر طريقه الدفع", Toast.LENGTH_LONG).show();
                            }
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
}

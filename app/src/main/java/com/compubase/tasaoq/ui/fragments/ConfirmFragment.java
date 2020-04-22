package com.compubase.tasaoq.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
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

import static android.app.Activity.RESULT_OK;
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
    @BindView(R.id.sp_city)
    Spinner spCity;

    private Realm realm;

    private List<ProductsModel> productsModelList = new ArrayList<>();
    private CartAdapter cartAdapter;
    private ArrayList<ProductsModel> data = new ArrayList<>();
    private double price;
    private SharedPreferences preferences;
    private String id;
    private String item;

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
            price = getArguments().getDouble("price");
        }

        txtTotalValue.setText(String.valueOf(price));
        Realm.init(Objects.requireNonNull(getContext()));
        realm = Realm.getDefaultInstance();


        List<String> governorate = new ArrayList<>();
        governorate.add("اختر المدينة");
        governorate.add("جده");
        governorate.add("مكه");
        governorate.add("محايل عسير");

        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,governorate);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCity.setAdapter(arrayAdapter);
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                item = governorate.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



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

        if (item.equals("0")) {

            Toast.makeText(getActivity(), "اختر المدينة", Toast.LENGTH_SHORT).show();
        }else {

            functionVolly();

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PaymentParams.PAYMENT_REQUEST_CODE) {
            Log.e("Tag", data.getStringExtra(PaymentParams.RESPONSE_CODE));
            Log.e("Tag", data.getStringExtra(PaymentParams.TRANSACTION_ID));

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    RealmResults<ProductsModel> result = realm.where(ProductsModel.class).findAll();
                    result.deleteAllFromRealm();
                }
            });

            startActivity(new Intent(getContext(), HomeActivity.class));
            Objects.requireNonNull(getActivity()).finish();

            
            if (data.hasExtra(PaymentParams.TOKEN) && !data.getStringExtra(PaymentParams.TOKEN).isEmpty()) {
                Log.e("Tag", data.getStringExtra(PaymentParams.TOKEN));
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_EMAIL));
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD));


            }
        }
    }


    private void Payment() {

        Intent in = new Intent(getActivity(), PayTabActivity.class);
        in.putExtra(PaymentParams.MERCHANT_EMAIL, "pay@lammah.net"); //this a demo account for testing the sdk
        in.putExtra(PaymentParams.SECRET_KEY, "HjFY9qb2tsFo5Kfy04jO77OGq7tgXDNEXtC7UOangPMlA2UA7doekdVLko1h2PW0iz7cniAD8mZIpTiw7HKrVFcf4dx72u7l3fxq");//Add your Secret Key Here
        in.putExtra(PaymentParams.LANGUAGE, PaymentParams.ENGLISH);
        in.putExtra(PaymentParams.TRANSACTION_TITLE, "Payment");
        in.putExtra(PaymentParams.AMOUNT, Double.valueOf(price));

        in.putExtra(PaymentParams.CURRENCY_CODE, "SAR");
        in.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, "00966537376868");
        in.putExtra(PaymentParams.CUSTOMER_EMAIL, "pay@lammah.net");
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

        String address = edAddress.getText().toString(); // Shof B2a Btgebo Mnen

        String fullAddress = item + " " + address;

        StringBuilder GET_JSON_DATA_HTTP_URL =
                new StringBuilder("http://fastini.alosboiya.com.sa/store_app.asmx/insert_orders?id_user=" +
                        id + "&address=" + fullAddress + "&totle_price=" + price);


        for (int i = 0; i <= productsModelList.size() - 1; i++) {
            GET_JSON_DATA_HTTP_URL.append("&id_product=").append(String.valueOf(productsModelList.get(i).getId()));
        }

        if (TextUtils.isEmpty(edAddress.getText().toString())) {
            edAddress.setError("ادخل عنوانك");
            edAddress.requestFocus();
        } else if (searchRadioBtn.isChecked() || advSearchRadioBtn.isChecked()) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_JSON_DATA_HTTP_URL.toString(),

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.equals("True")) {


                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(@NonNull Realm realm) {
                                        RealmResults<ProductsModel> result = realm.where(ProductsModel.class).findAll();
                                        result.deleteAllFromRealm();
                                    }
                                });


                                if (advSearchRadioBtn.isChecked()) {
                                    Payment();
                                } else {
                                    Toast.makeText(getContext(), "تم ارسال الطلب", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getContext(), HomeActivity.class));
                                    Objects.requireNonNull(getActivity()).finish();
                                }
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }

            });

            RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);

        } else
            Toast.makeText(getContext(), "من فضلك اختر طريقه الدفع", Toast.LENGTH_LONG).show();

    }
}

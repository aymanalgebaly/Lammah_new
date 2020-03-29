package com.compubase.tasaoq.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.compubase.tasaoq.R;
import com.compubase.tasaoq.data.API;
import com.compubase.tasaoq.helper.RetrofitClient;
import com.compubase.tasaoq.ui.activities.HomeActivity;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    @BindView(R.id.input_username)
    EditText inputUsername;
    @BindView(R.id.input_mail)
    EditText inputMail;
    @BindView(R.id.input_mobile_number)
    EditText inputMobileNumber;
    Unbinder unbinder;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.input_pass)
    EditText inputPass;
    @BindView(R.id.input_con_password)
    EditText inputConPassword;

    private SharedPreferences preferences;
    private String phone, name, email, id;
    private String pass;
    private String con_pass;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);

        preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE);
        name = preferences.getString("name", "");
        email = preferences.getString("email", "");
        phone = preferences.getString("phone", "");
        id = preferences.getString("id", "");
        pass = preferences.getString("password", "");
        con_pass = preferences.getString("con_pass", "");

        inputUsername.setText(name);
        inputMail.setText(email);
        inputMobileNumber.setText(phone);
        inputPass.setText(pass);

        return view;
    }

    private void updateData() {
        RetrofitClient.getInstant().create(API.class).updateProfile(name, email, phone, pass, "img", id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        String string = response.body().string();
                        if (string.equals("True")) {

                            Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), HomeActivity.class));
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        inputUsername.setText(name);
        inputMail.setText(email);
        inputMobileNumber.setText(phone);
        inputPass.setText(pass);
        inputConPassword.setText(con_pass);
    }

    @OnClick({R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                updateData();
                break;
        }
    }
}

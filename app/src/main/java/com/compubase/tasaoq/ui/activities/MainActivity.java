package com.compubase.tasaoq.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.compubase.tasaoq.R;
import com.compubase.tasaoq.data.API;
import com.compubase.tasaoq.helper.RetrofitClient;
import com.compubase.tasaoq.model.LoginModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.lin_one)
    LinearLayout linOne;
    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.txt_forgot)
    TextView txtForgot;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.txt_register)
    TextView txtRegister;

    @BindView(R.id.lin_two)
    LinearLayout linTwo;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private Integer id;
    private String name, mail, phonenumber, img;
    private SharedPreferences preferences;
    private boolean login_user;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        preferences = getSharedPreferences("user", MODE_PRIVATE);

        progressBar.setVisibility(View.GONE);
    }

    @OnClick({R.id.txt_forgot, R.id.btn_login, R.id.txt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_forgot:
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.btn_login:
                loginValidate();
                break;
            case R.id.txt_register:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
        }
    }

    private void loginValidate() {
        String username = edUsername.getText().toString();
        pass = edPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            edUsername.setError("اسم المستخدم مطلوب");
        } else if (TextUtils.isEmpty(pass)) {
            edPassword.setError("كلمة المرور مطلوبه");
        } else {

            Call<ResponseBody> call2 = RetrofitClient.getInstant().create(API.class).UserLogin(username, pass);

            call2.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();

                    assert response.body() != null;
                    try {

                        List<LoginModel> loginModelList =
                                Arrays.asList(gson.fromJson(response.body().string(), LoginModel[].class));
                        if (response.isSuccessful()) {

                            progressBar.setVisibility(View.VISIBLE);

                            name = loginModelList.get(0).getName();
                            mail = loginModelList.get(0).getEmail();
                            id = loginModelList.get(0).getId();
                            img = loginModelList.get(0).getImg();
                            phonenumber = loginModelList.get(0).getPhone();

                            sharedLogin();

                            startActivity(new Intent(MainActivity.this,HomeActivity.class));

                            finish();

                        }

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "خطأ في البريد الالكتروني او كلمة المرور", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sharedLogin() {
        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();

        preferences = getSharedPreferences("user", MODE_PRIVATE);

        editor.putBoolean("login", true);

        editor.putString("id", String.valueOf(id));
        editor.putString("name", name);
        editor.putString("email", mail);
        editor.putString("phone", phonenumber);
        editor.putString("image", img);
        editor.putString("password", pass);

        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}


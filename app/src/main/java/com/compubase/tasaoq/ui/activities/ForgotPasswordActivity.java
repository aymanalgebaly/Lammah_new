package com.compubase.tasaoq.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.compubase.tasaoq.R;
import com.compubase.tasaoq.data.API;
import com.compubase.tasaoq.helper.RetrofitClient;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.lin_two)
    LinearLayout linTwo;
    @BindView(R.id.rel)
    RelativeLayout rel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {

        progressBar.setVisibility(View.VISIBLE);
        String mail = edUsername.getText().toString();

        if (TextUtils.isEmpty(mail)){
            edUsername.setError("ادخل البريد الالكتروني");
        }else {

            RetrofitClient.getInstant().create(API.class).forgete_password_by_email(mail).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()){

                        try {
                            assert response.body() != null;
                            String string = response.body().string();

                            progressBar.setVisibility(View.GONE);

                            if (string.equals("True")){
                                startActivity(new Intent(ForgotPasswordActivity.this,MainActivity.class));
                                Toast.makeText(ForgotPasswordActivity.this, "تم ارسال كلمة المرور الجديده",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }else if (string.equals("False")){

                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(ForgotPasswordActivity.this, "تاكد من صحة البريد الالكتروني", Toast.LENGTH_SHORT).show();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }
}

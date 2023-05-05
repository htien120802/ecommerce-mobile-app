package com.example.ecommerce_mobile_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.ActivitySignInBinding;
import com.example.ecommerce_mobile_app.model.SignInRequest;
import com.example.ecommerce_mobile_app.model.BaseResponse;
import com.example.ecommerce_mobile_app.util.PrefManager;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding activitySignInBinding;
    private String username, password;
    private PrefManager prefManager = new PrefManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignInBinding = ActivitySignInBinding.inflate(LayoutInflater.from(getApplicationContext()));
        setContentView(activitySignInBinding.getRoot());

        activitySignInBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = activitySignInBinding.etEmailSignIn.getText().toString().trim();
                password = activitySignInBinding.etPasswordSignIn.getText().toString();
                doSignIn();
            }
        });

        activitySignInBinding.tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void doSignIn(){
        RetrofitClient.getInstance().signIn(new SignInRequest(username,password)).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getData()!= null){
                        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                        Gson gson = new Gson();
                        String customer = gson.toJson(response.body().getData());
                        prefManager.saveLoginUser(customer);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),response.body().getResponse_description(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

}
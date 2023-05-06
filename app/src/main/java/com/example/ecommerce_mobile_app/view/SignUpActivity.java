package com.example.ecommerce_mobile_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.ActivitySignUpBinding;
import com.example.ecommerce_mobile_app.model.BaseResponse;
import com.example.ecommerce_mobile_app.model.Customer;
import com.example.ecommerce_mobile_app.model.SignUpRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding activitySignUpBinding;
    private SignUpRequest signUpRequest = new SignUpRequest();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = ActivitySignUpBinding.inflate(LayoutInflater.from(getApplicationContext()));
        setContentView(activitySignUpBinding.getRoot());

        activitySignUpBinding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpRequest.setFirstName(activitySignUpBinding.etFirstName.getText().toString());
                signUpRequest.setLastName(activitySignUpBinding.etLastName.getText().toString());
                signUpRequest.setUsername(activitySignUpBinding.etUserName.getText().toString());
                signUpRequest.setEmail(activitySignUpBinding.etEmail.getText().toString());
                signUpRequest.setPassword(activitySignUpBinding.etPasswordSignUp.getText().toString());
                if (activitySignUpBinding.etConfirmPwSignUp.getText().toString().equals(signUpRequest.getPassword())){
                    doSignUp();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Password and confirm password doesn't match!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        activitySignUpBinding.tvBelowWelcomeSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        activitySignUpBinding.btnBackSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpActivity.super.onBackPressed();
            }
        });
    }
    public void doSignUp(){
        RetrofitClient.getInstance().signUp(signUpRequest).enqueue(new Callback<BaseResponse<Customer>>() {
            @Override
            public void onResponse(Call<BaseResponse<Customer>> call, Response<BaseResponse<Customer>> response) {
                if (response.isSuccessful()){
                    if (response.body().getData() != null){
                        Toast.makeText(getApplicationContext(),response.body().getResponse_description(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),response.body().getResponse_description(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Customer>> call, Throwable t) {

            }
        });
    }
}
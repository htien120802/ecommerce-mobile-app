package com.example.ecommerce_mobile_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.ActivityChangePasswordBinding;
import com.example.ecommerce_mobile_app.model.BaseResponse;
import com.example.ecommerce_mobile_app.model.Customer;
import com.example.ecommerce_mobile_app.model.SignInRequest;
import com.example.ecommerce_mobile_app.model.UpdatePasswordRequest;
import com.example.ecommerce_mobile_app.util.CustomToast;
import com.example.ecommerce_mobile_app.util.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.http.HTTP;

public class UpdatePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding activityChangePasswordBinding;

    private String oldPassword, newPassword, confirmPassword;
    private PrefManager prefManager = new PrefManager(this);

    private UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChangePasswordBinding = ActivityChangePasswordBinding.inflate(LayoutInflater.from(getApplicationContext()));
        setContentView(activityChangePasswordBinding.getRoot());

        activityChangePasswordBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPassword = activityChangePasswordBinding.etPasswordChange.getText().toString().trim();
                newPassword = activityChangePasswordBinding.etNewPasswordChange.getText().toString();
                confirmPassword = activityChangePasswordBinding.etConfirmPasswordChange.getText().toString();
                updatePasswordRequest.setOldPassword(oldPassword);
                updatePasswordRequest.setNewPassword(newPassword);
                updatePasswordRequest.setConfirmPassword(confirmPassword);
                doChangePassword();
            }
        });
    }

    public void doChangePassword(){
        RetrofitClient.getInstance().updatePassword(prefManager.getCustomer().getId(), updatePasswordRequest).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResponse_message().equals("Success")) {
                        CustomToast.showSuccessMessage(getApplicationContext(), response.body().getResponse_description());
                        //UpdatePasswordActivity.super.onBackPressed();
                    } else {
                        CustomToast.showFailMessage(getApplicationContext(), response.body().getResponse_description());
                    }
                } else {
                    CustomToast.showFailMessage(getApplicationContext(), "INTERNAL_SERVER_ERROR");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

            }
        });
    }
}

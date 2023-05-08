package com.example.ecommerce_mobile_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.ecommerce_mobile_app.databinding.ActivityPersonalDetailsBinding;
import com.example.ecommerce_mobile_app.util.PrefManager;

public class CustomerDetailActivity extends AppCompatActivity {
    ActivityPersonalDetailsBinding activityPersonalDetailsBinding;
    PrefManager prefManager = new PrefManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPersonalDetailsBinding = ActivityPersonalDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityPersonalDetailsBinding.getRoot());

        activityPersonalDetailsBinding.setCustomer(prefManager.getCustomer());
        activityPersonalDetailsBinding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerDetailActivity.super.onBackPressed();
            }
        });
    }
}
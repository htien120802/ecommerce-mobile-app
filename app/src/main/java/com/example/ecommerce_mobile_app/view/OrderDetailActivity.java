package com.example.ecommerce_mobile_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.ecommerce_mobile_app.databinding.ActivityMyOrderDetailsBinding;
import com.example.ecommerce_mobile_app.model.Order;
import com.google.gson.Gson;

public class OrderDetailActivity extends AppCompatActivity {
    private ActivityMyOrderDetailsBinding activityMyOrderDetailsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyOrderDetailsBinding = ActivityMyOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityMyOrderDetailsBinding.getRoot());
        Order order = new Gson().fromJson(getIntent().getExtras().getString("order"),Order.class);
        activityMyOrderDetailsBinding.setOrder(order);

        activityMyOrderDetailsBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailActivity.super.onBackPressed();
            }
        });
    }
}
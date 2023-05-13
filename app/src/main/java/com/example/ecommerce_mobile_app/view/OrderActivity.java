package com.example.ecommerce_mobile_app.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ecommerce_mobile_app.adapter.OrderAdapter;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.ActivityMyOrderBinding;
import com.example.ecommerce_mobile_app.model.BaseResponse;
import com.example.ecommerce_mobile_app.model.Customer;
import com.example.ecommerce_mobile_app.model.Order;
import com.example.ecommerce_mobile_app.model.OrderDetail;
import com.example.ecommerce_mobile_app.util.CustomToast;
import com.example.ecommerce_mobile_app.util.PrefManager;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
    private ActivityMyOrderBinding activityMyOrderBinding;
    private PrefManager prefManager = new PrefManager(this);

    private OrderAdapter orderAdapter = new OrderAdapter();
    Customer customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyOrderBinding = ActivityMyOrderBinding.inflate(getLayoutInflater());
        setContentView(activityMyOrderBinding.getRoot());
        customer = prefManager.getCustomer();
        orderAdapter.setiOnClickItem(new OrderAdapter.IOnClickItem() {
            @Override
            public void clickItem(Order order) {
                Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
                intent.putExtra("order",new Gson().toJson(order));
                startActivity(intent);
            }
        });
        setListOrder(customer.getId());

        activityMyOrderBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderActivity.super.onBackPressed();
            }
        });
    }

    public void setListOrder(int id){
        RetrofitClient.getInstance().getOrders(id).enqueue(new Callback<BaseResponse<List<Order>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Order>>> call, Response<BaseResponse<List<Order>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse_message().equals("Success")){
                        orderAdapter.setmListOrder(response.body().getData());
                        activityMyOrderBinding.rvMyOrderList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        activityMyOrderBinding.rvMyOrderList.setAdapter(orderAdapter);
                    }
                    else {
                        CustomToast.showFailMessage(getApplicationContext(),response.body().getResponse_description());
                    }
                }
                else {
                    CustomToast.showFailMessage(getApplicationContext(),"Loading your order is failure!");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Order>>> call, Throwable t) {

            }
        });
    }
}
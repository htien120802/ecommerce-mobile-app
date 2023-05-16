package com.example.ecommerce_mobile_app.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.ecommerce_mobile_app.adapter.LineProductAdapter;
import com.example.ecommerce_mobile_app.adapter.OrderAdapter;
import com.example.ecommerce_mobile_app.databinding.ActivityMyOrderDetailsBinding;
import com.example.ecommerce_mobile_app.model.CartItem;
import com.example.ecommerce_mobile_app.model.Order;
import com.example.ecommerce_mobile_app.model.OrderDetail;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    private ActivityMyOrderDetailsBinding activityMyOrderDetailsBinding;
    private Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyOrderDetailsBinding = ActivityMyOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityMyOrderDetailsBinding.getRoot());
        order = new Gson().fromJson(getIntent().getExtras().getString("order"),Order.class);
        activityMyOrderDetailsBinding.setOrder(order);
        setListProduct();
        activityMyOrderDetailsBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailActivity.super.onBackPressed();
            }
        });
    }
    public void setListProduct(){
        LineProductAdapter lineProductAdapter = new LineProductAdapter();
        List<CartItem> cartItems = convertListOrderDetailToListCart(order.getOrderDetails());
        lineProductAdapter.setmListCartItems(cartItems);
        activityMyOrderDetailsBinding.rvMyOrderListProduct.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        activityMyOrderDetailsBinding.rvMyOrderListProduct.setAdapter(lineProductAdapter);
    }
    public List<CartItem> convertListOrderDetailToListCart(List<OrderDetail> orderDetails){
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        for (OrderDetail orderDetail : orderDetails){
            cartItem.setShortName(orderDetail.getProduct().getShortName());
            cartItem.setMobileName(orderDetail.getProduct().getMobileName());
            cartItem.setProductId(orderDetail.getProduct().getId());
            cartItem.setMainImagePath(orderDetail.getProduct().getMainImagePath());
            cartItem.setQuantity(orderDetail.getQuantity());
            cartItem.setSubtotal(orderDetail.getSubtotal());
            cartItems.add(cartItem);
        }
        return cartItems;
    }
}
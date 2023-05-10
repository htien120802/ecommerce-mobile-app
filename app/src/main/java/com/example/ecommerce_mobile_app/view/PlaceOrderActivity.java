package com.example.ecommerce_mobile_app.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.ecommerce_mobile_app.adapter.LineProductAdapter;
import com.example.ecommerce_mobile_app.databinding.ActivityPlaceOrderBinding;
import com.example.ecommerce_mobile_app.model.CartItem;
import com.example.ecommerce_mobile_app.model.Customer;
import com.example.ecommerce_mobile_app.model.InfoCart;
import com.example.ecommerce_mobile_app.util.PrefManager;

import java.util.List;

public class PlaceOrderActivity extends AppCompatActivity {
    ActivityPlaceOrderBinding activityPlaceOrderBinding;
    PrefManager prefManager = new PrefManager(this);
    Customer customer;
    List<CartItem> cartItemList;
    LineProductAdapter lineProductAdapter = new LineProductAdapter();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPlaceOrderBinding = ActivityPlaceOrderBinding.inflate(getLayoutInflater());
        setContentView(activityPlaceOrderBinding.getRoot());

        customer = prefManager.getCustomer();
        activityPlaceOrderBinding.setCustomer(customer);

        activityPlaceOrderBinding.setInfoCart((InfoCart) getIntent().getSerializableExtra("infoCart"));

        recyclerView = activityPlaceOrderBinding.rvProductOrder;
        cartItemList = (List<CartItem>) getIntent().getSerializableExtra("listCartItems");
        lineProductAdapter.setmListCartItems(cartItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(lineProductAdapter);

        activityPlaceOrderBinding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceOrderActivity.super.onBackPressed();
            }
        });
    }
}
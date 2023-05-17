package com.example.ecommerce_mobile_app.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.example.ecommerce_mobile_app.adapter.LineProductAdapter;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.ActivityPlaceOrderBinding;
import com.example.ecommerce_mobile_app.model.BaseResponse;
import com.example.ecommerce_mobile_app.model.CartItem;
import com.example.ecommerce_mobile_app.model.Customer;
import com.example.ecommerce_mobile_app.model.InfoCart;
import com.example.ecommerce_mobile_app.util.CustomToast;
import com.example.ecommerce_mobile_app.util.OrderSuccessDialog;
import com.example.ecommerce_mobile_app.util.PrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceOrderActivity extends AppCompatActivity {
    ActivityPlaceOrderBinding activityPlaceOrderBinding;
    PrefManager prefManager = new PrefManager(this);
    Customer customer;
    List<CartItem> cartItemList;
    LineProductAdapter lineProductAdapter = new LineProductAdapter();
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
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

        activityPlaceOrderBinding.btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(PlaceOrderActivity.this);
                progressDialog.setMessage("Wait a second...");
                placeOrder();
            }
        });
    }
    public void placeOrder(){
        progressDialog.show();
        RetrofitClient.getInstance().placeOrder(new PrefManager(getApplicationContext()).getCustomer().getId()).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    if (response.body().getResponse_message().equals("Success")){
                        CustomToast.showSuccessMessage(getApplicationContext(),response.body().getResponse_description());
                        OrderSuccessDialog orderSuccessDialog = new OrderSuccessDialog();
                        orderSuccessDialog.show(getSupportFragmentManager(),"Order success");
                    }
                    else
                        CustomToast.showFailMessage(getApplicationContext(),response.body().getResponse_description());
                }
                else {
                    progressDialog.dismiss();
                    CustomToast.showFailMessage(getApplicationContext(),"Order is unsuccessful!");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

            }
        });
    }
}
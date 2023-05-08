package com.example.ecommerce_mobile_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce_mobile_app.adapter.ImageAdapter;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.ActivityItemDetailsBinding;
import com.example.ecommerce_mobile_app.model.BaseResponse;
import com.example.ecommerce_mobile_app.model.CartItem;
import com.example.ecommerce_mobile_app.model.Image;
import com.example.ecommerce_mobile_app.model.Product;
import com.example.ecommerce_mobile_app.util.CustomToast;
import com.example.ecommerce_mobile_app.util.PrefManager;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    ActivityItemDetailsBinding activityItemDetailsBinding;
    private int productId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityItemDetailsBinding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
        Bundle bundle = getIntent().getExtras();
        productId = (int) bundle.getSerializable("product_id");
        getProduct(productId);

        activityItemDetailsBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetailActivity.super.onBackPressed();
            }
        });

        activityItemDetailsBinding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this,MainActivity.class);
                bundle.clear();
                bundle.putSerializable("change_to","cart");
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        activityItemDetailsBinding.btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCartItem(productId);
            }
        });
        setContentView(activityItemDetailsBinding.getRoot());
    }
    public void getProduct(int productId){
        RetrofitClient.getInstance().getProductById(productId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()){
                    Product product = response.body();
                    activityItemDetailsBinding.setProduct(product);

                    List<Image> images = new ArrayList<>();
                    images.add(new Image(product.getMainImagePath()));
                    for (Image image : product.getImages()){
                        images.add(new Image(image.getImagePath()));
                    }
                    ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext(), images);
                    activityItemDetailsBinding.viewPagerItemDetails.setAdapter(imageAdapter);
                    activityItemDetailsBinding.circleIndicator.setViewPager(activityItemDetailsBinding.viewPagerItemDetails);
                    imageAdapter.registerDataSetObserver(activityItemDetailsBinding.circleIndicator.getDataSetObserver());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {

            }
        });
    }
    public void addCartItem(int prodcutId){
        RetrofitClient.getInstance().addCartItem(new PrefManager(this).getCustomer().getId(), prodcutId,1).enqueue(new Callback<BaseResponse<List<CartItem>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<CartItem>>> call, Response<BaseResponse<List<CartItem>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse_message().equals("Success"))
                        CustomToast.showSuccessMessage(getApplicationContext(),response.body().getResponse_description());
                    else
                        CustomToast.showFailMessage(getApplicationContext(),response.body().getResponse_description());
                }
                else {
                    CustomToast.showFailMessage(getApplicationContext(),"Add product to cart is unsuccessful!");
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<List<CartItem>>> call, Throwable t) {

            }
        });
    }
}
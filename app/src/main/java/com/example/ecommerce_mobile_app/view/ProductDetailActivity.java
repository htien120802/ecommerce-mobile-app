package com.example.ecommerce_mobile_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce_mobile_app.adapter.ImageAdapter;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.ActivityItemDetailsBinding;
import com.example.ecommerce_mobile_app.model.Image;
import com.example.ecommerce_mobile_app.model.Product;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    ActivityItemDetailsBinding activityItemDetailsBinding;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityItemDetailsBinding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
        Bundle bundle = getIntent().getExtras();
        id = (int) bundle.getSerializable("product_id");
        callAPI();

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
        setContentView(activityItemDetailsBinding.getRoot());
    }
    public void callAPI(){
        RetrofitClient.getInstance().getProductById(id).enqueue(new Callback<Product>() {
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
}
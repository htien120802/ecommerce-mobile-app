package com.example.ecommerce_mobile_app.viewmodel;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_mobile_app.adapter.CategoryAdapter;
import com.example.ecommerce_mobile_app.adapter.ProductAdapter;
import com.example.ecommerce_mobile_app.api.APIService;
import com.example.ecommerce_mobile_app.api.CONSTANT;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.model.Category;
import com.example.ecommerce_mobile_app.model.Product;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentStoreViewModel {

    private static List<Category> mListCategories;
    private static List<Product> mListProducts;
    private static ProgressBar progressBar;
    @BindingAdapter("initList")
    public static void setList(RecyclerView recyclerView, int type){

        if (type == 1){
            getListCategories(recyclerView);
        }
        else {
            getListProducts(recyclerView);
        }
    }
    public static void getListCategories(RecyclerView recyclerView){
        RetrofitClient.getInstance().getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()){
                    mListCategories = response.body().subList(0,11);
                    Category category = new Category(0,"All");
                    mListCategories.add(0,category);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(),LinearLayoutManager.HORIZONTAL,false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    CategoryAdapter categoryAdapter = new CategoryAdapter(mListCategories,recyclerView.getContext());
                    recyclerView.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
            }
        });
    }
    public static void getListProducts(RecyclerView recyclerView){
        RetrofitClient.getInstance().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()){
                    mListProducts = response.body();
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(),2);
                    ProductAdapter productAdapter = new ProductAdapter(mListProducts,recyclerView.getContext());
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(productAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
    }
    @BindingAdapter("setImage")
    public static void setImage(ImageView shapeableImageView, String imagePath){
        imagePath = imagePath.replace("http://localhost:8081/", CONSTANT.BASE_URL);
        Glide.with(shapeableImageView.getContext()).load(imagePath).into(shapeableImageView);
    }

}

package com.example.ecommerce_mobile_app.viewmodel;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_mobile_app.adapter.CategoryAdapter;
import com.example.ecommerce_mobile_app.adapter.ProductAdapter;
import com.example.ecommerce_mobile_app.api.APIService;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.model.Category;
import com.example.ecommerce_mobile_app.model.Product;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentStoreViewModel {

    private static List<Category> mListCategories;
    private static List<Product> mListProducts;
    @BindingAdapter("initListCategories")
    public static void setListCategories(RecyclerView recyclerView, boolean set){
        if (set == true){
            getListCategories(recyclerView,set);
        }
    }
    @BindingAdapter("initListProducts")
    public static void setListProducts(RecyclerView recyclerView, boolean set){
        if (set == true){
            getListProducts(recyclerView,set);
        }
    }
    public static void getListCategories(RecyclerView recyclerView, boolean set){
        RetrofitClient.getInstance().getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()){
                    mListCategories = response.body();
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
    public static void getListProducts(RecyclerView recyclerView, boolean set){
        RetrofitClient.getInstance().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()){
                    mListProducts = response.body();
                    Log.d("AAAAA",""+mListProducts.size());
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(),2);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    ProductAdapter productAdapter = new ProductAdapter(mListProducts);
                    recyclerView.setAdapter(productAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
    }
    @BindingAdapter("setImage")
    public static void setImage(ImageView imageView, String imagePath){
        Glide.with(imageView.getContext()).load(imagePath).into(imageView);
    }
}

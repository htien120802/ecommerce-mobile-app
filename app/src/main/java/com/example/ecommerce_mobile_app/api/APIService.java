package com.example.ecommerce_mobile_app.api;

import com.example.ecommerce_mobile_app.model.Category;
import com.example.ecommerce_mobile_app.model.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface APIService {
//    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
//    APIService apiService = new Retrofit.Builder()
//            .baseUrl("http://192.168.1.28:8081/")
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//            .create(APIService.class);
    @GET("api/categories")
    Call<List<Category>> getCategories();

    @GET("api/products")
    Call<List<Product>> getProducts();
}

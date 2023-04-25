package com.example.ecommerce_mobile_app.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.adapter.CategoryAdapter;
import com.example.ecommerce_mobile_app.adapter.ProductAdapter;
import com.example.ecommerce_mobile_app.api.APIService;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.FragmentStoreBinding;
import com.example.ecommerce_mobile_app.model.Category;
import com.example.ecommerce_mobile_app.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoreFragment extends Fragment {
    FragmentStoreBinding fragmentStoreBinding;
    List<Category> mListCategories;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentStoreBinding = FragmentStoreBinding.inflate(inflater,container,false);
        RetrofitClient.getInstance().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()){
                    List<Product> products = response.body();
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
                    fragmentStoreBinding.rvListItem.setLayoutManager(gridLayoutManager);
                    ProductAdapter productAdapter = new ProductAdapter(products);
                    fragmentStoreBinding.rvListItem.setAdapter(productAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
        return fragmentStoreBinding.getRoot();
    }

}
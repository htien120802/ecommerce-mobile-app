package com.example.ecommerce_mobile_app.view.fragment;

import android.os.Bundle;

import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.adapter.CategoryAdapter;
import com.example.ecommerce_mobile_app.adapter.BoxProductAdapter;
import com.example.ecommerce_mobile_app.api.CONSTANT;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.FragmentStoreBinding;
import com.example.ecommerce_mobile_app.model.Category;
import com.example.ecommerce_mobile_app.model.Product;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoreFragment extends Fragment {
    FragmentStoreBinding fragmentStoreBinding;
    private List<Category> mListCategories;
    private List<Product> mListProducts;
    private CategoryAdapter categoryAdapter;
    private BoxProductAdapter boxProductAdapter;
    private RecyclerView rcvCategory, rcvProduct;
    private String keySearch = "";
    private String categoryFilter = "All";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentStoreBinding = FragmentStoreBinding.inflate(inflater,container,false);
        rcvCategory = fragmentStoreBinding.rvCategoryStore;
        rcvProduct = fragmentStoreBinding.rvListItem;
        setListCategories();
        setListProducts();
        fragmentStoreBinding.etSearchHome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keySearch = charSequence.toString();
                boxProductAdapter.doFilter(categoryFilter,keySearch);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return fragmentStoreBinding.getRoot();
    }
    public  void setListCategories(){
        RetrofitClient.getInstance().getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()){
                    mListCategories = response.body().subList(0,11);
                    Category category = new Category(0,"All");
                    mListCategories.add(0,category);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                    rcvCategory.setLayoutManager(linearLayoutManager);
                    categoryAdapter = new CategoryAdapter(mListCategories, getContext(), new CategoryAdapter.IOnItemClickListener() {
                        @Override
                        public void onItemClickListener(Category category) {
                            categoryFilter = category.getName();
                            boxProductAdapter.doFilter(categoryFilter,keySearch);
                        }
                    });
                    rcvCategory.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
            }
        });
    }
    public void setListProducts(){
        RetrofitClient.getInstance().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()){
                    mListProducts = response.body();
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
                    boxProductAdapter = new BoxProductAdapter();
                    boxProductAdapter.setmListProducts(mListProducts);
                    boxProductAdapter.setContext(getContext());
                    rcvProduct.setLayoutManager(gridLayoutManager);
                    rcvProduct.setAdapter(boxProductAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });


    }

}
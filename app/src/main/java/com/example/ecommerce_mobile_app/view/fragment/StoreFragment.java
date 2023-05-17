package com.example.ecommerce_mobile_app.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce_mobile_app.adapter.CategoryAdapter;
import com.example.ecommerce_mobile_app.adapter.BoxProductAdapter;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.FragmentStoreBinding;
import com.example.ecommerce_mobile_app.model.response.BaseResponse;
import com.example.ecommerce_mobile_app.model.Category;
import com.example.ecommerce_mobile_app.model.Product;
import com.example.ecommerce_mobile_app.model.WishlistItem;
import com.example.ecommerce_mobile_app.util.PrefManager;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoreFragment extends Fragment {
    FragmentStoreBinding fragmentStoreBinding;
    private List<Category> mListCategories;
    private List<Product> mListProducts;

    private List<WishlistItem> mListFavProducts;
    private CategoryAdapter categoryAdapter;
    private BoxProductAdapter boxProductAdapter = new BoxProductAdapter();
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
        getListFavProduct();
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
    public void getListFavProduct(){
        RetrofitClient.getInstance().getWishlist(new PrefManager(getContext()).getCustomer().getId()).enqueue(new Callback<BaseResponse<List<WishlistItem>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<WishlistItem>>> call, Response<BaseResponse<List<WishlistItem>>> response) {
                if (response.isSuccessful() && response.body().getResponse_message().equals("Success")){
                    mListFavProducts = response.body().getData();
                    boxProductAdapter.setmListFavProducts(mListFavProducts);
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<List<WishlistItem>>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getListFavProduct();
    }
}
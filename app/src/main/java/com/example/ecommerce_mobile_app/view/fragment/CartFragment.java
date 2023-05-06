package com.example.ecommerce_mobile_app.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.adapter.CartItemAdapter;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.FragmentCartBinding;
import com.example.ecommerce_mobile_app.model.BaseResponse;
import com.example.ecommerce_mobile_app.model.CartItem;
import com.example.ecommerce_mobile_app.util.PrefManager;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {

    FragmentCartBinding fragmentCartBinding;
    
    List<CartItem> mListCartItems;
    RecyclerView recyclerView;
    CartItemAdapter cartItemAdapter = new CartItemAdapter();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCartBinding = FragmentCartBinding.inflate(inflater,container,false);
        recyclerView = fragmentCartBinding.rvListCart;
        setCart();
        return fragmentCartBinding.getRoot();
    }
    public void setCart(){
        int customerId = new PrefManager(getContext()).getCustomer().getId();
        RetrofitClient.getInstance().getCart(customerId).enqueue(new Callback<BaseResponse<List<CartItem>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<CartItem>>> call, Response<BaseResponse<List<CartItem>>> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (Objects.equals(response.body().getResponse_message(), "Success")){
                        Log.e("ERR",response.body().getData().get(1).getShortName());
                        mListCartItems = response.body().getData();
                        cartItemAdapter.setmListCartItems(mListCartItems);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(cartItemAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<CartItem>>> call, Throwable t) {

            }
        });
    }
}
package com.example.ecommerce_mobile_app.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.adapter.CartItemAdapter;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.FragmentCartBinding;
import com.example.ecommerce_mobile_app.model.BaseResponse;
import com.example.ecommerce_mobile_app.model.CartItem;
import com.example.ecommerce_mobile_app.model.InfoCart;
import com.example.ecommerce_mobile_app.util.CustomDialog;
import com.example.ecommerce_mobile_app.util.CustomToast;
import com.example.ecommerce_mobile_app.util.PrefManager;
import com.example.ecommerce_mobile_app.view.MainActivity;
import com.example.ecommerce_mobile_app.view.ProductDetailActivity;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {

    FragmentCartBinding fragmentCartBinding;
    
    List<CartItem> mListCartItems;
    RecyclerView recyclerView;
    CartItemAdapter cartItemAdapter;

    private final InfoCart infoCart = new InfoCart();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
            public void onResponse(@NonNull Call<BaseResponse<List<CartItem>>> call, Response<BaseResponse<List<CartItem>>> response) {
                if (response.isSuccessful()){

                    assert response.body() != null;
                    if (response.body().getResponse_message().equals("Success")){
                        mListCartItems = response.body().getData();
                        cartItemAdapter = new CartItemAdapter(new CartItemAdapter.IClickOnCartItem() {
                            @Override
                            public void clickMinus(CartItem cartItem) {
                                if (cartItem.getQuantity() == 1){
                                    clickDelete(cartItemAdapter.getmListCartItems(),cartItem);
                                }else {
                                    infoCart.setTotalPrice(infoCart.getTotalPrice() - cartItem.getSubtotal()/cartItem.getQuantity());
                                    cartItem.setSubtotal(cartItem.getSubtotal() - cartItem.getSubtotal()/cartItem.getQuantity());
                                    cartItem.setQuantity(cartItem.getQuantity()-1);
                                    updateCartItem(cartItem);
                                }
                            }

                            @Override
                            public void clickPlus(CartItem cartItem) {
                                infoCart.setTotalPrice(infoCart.getTotalPrice() + cartItem.getSubtotal()/cartItem.getQuantity());
                                cartItem.setSubtotal(cartItem.getSubtotal() + cartItem.getSubtotal()/cartItem.getQuantity());
                                cartItem.setQuantity(cartItem.getQuantity() + 1);
                                updateCartItem(cartItem);
                            }

                            @Override
                            public void clickDelete(List<CartItem> mListCartItems, CartItem cartItem) {
                                CustomDialog customDialog = new CustomDialog();
                                customDialog.setPositiveButton(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        customDialog.dismiss();
                                        infoCart.setTotalPrice(infoCart.getTotalPrice() - cartItem.getSubtotal());
                                        infoCart.setTotalItem(infoCart.getTotalItem() - 1);
                                        mListCartItems.remove(cartItem);
                                        deleteCartItem(cartItem);
                                    }
                                });
                                customDialog.setNegativeButton(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        customDialog.dismiss();
                                    }
                                });
                                customDialog.show(getActivity().getSupportFragmentManager(), "Delete cart item");
                            }

                            @Override
                            public void clickProduct(CartItem cartItem) {
                                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("product_id",cartItem.getProductId());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        cartItemAdapter.setmListCartItems(mListCartItems);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(cartItemAdapter);
                        infoCart.setTotalItem(mListCartItems.size());
                        infoCart.setTotalPrice(cartItemAdapter.calTotal());
                        fragmentCartBinding.setInfoCart(infoCart);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<List<CartItem>>> call, Throwable t) {

            }
        });
    }
    public void updateCartItem(CartItem cartItem){
        RetrofitClient.getInstance().updateCartItem(new PrefManager(getContext()).getCustomer().getId(),cartItem.getProductId(),cartItem.getQuantity()).enqueue(new Callback<BaseResponse<List<CartItem>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<CartItem>>> call, Response<BaseResponse<List<CartItem>>> response) {
                if (!response.isSuccessful()){
                    CustomToast.showFailMessage(getContext(),"Save cart is unsucessful");
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<List<CartItem>>> call, Throwable t) {

            }
        });
    }
    public void deleteCartItem(CartItem cartItem){
        RetrofitClient.getInstance().removeCartItem(new PrefManager(getContext()).getCustomer().getId(),cartItem.getProductId()).enqueue(new Callback<BaseResponse<List<CartItem>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<CartItem>>> call, Response<BaseResponse<List<CartItem>>> response) {

                if (response.isSuccessful()){
                    CustomToast.showSuccessMessage(getContext(),response.body().getResponse_description());
                }
                else {
                    CustomToast.showFailMessage(getContext(),"Delete product is unsuccessful!");
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<List<CartItem>>> call, Throwable t) {

            }
        });
    }
    @BindingAdapter("setTotalPrice")
    public static void setTotalPrice(TextView textView, float price){
        String format =  String.format("%.2f",price) + " $";
        textView.setText(format);
    }

    @BindingAdapter("setTotalItem")
    public static void setTotalItem(TextView textView, int item) {
        String format = String.format("Total: (%d items)",item);
        textView.setText(format);
    }

}
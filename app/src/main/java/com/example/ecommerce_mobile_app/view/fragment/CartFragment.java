package com.example.ecommerce_mobile_app.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.adapter.CartItemAdapter;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.FragmentCartBinding;
import com.example.ecommerce_mobile_app.model.response.BaseResponse;
import com.example.ecommerce_mobile_app.model.CartItem;
import com.example.ecommerce_mobile_app.model.Customer;
import com.example.ecommerce_mobile_app.model.InfoCart;
import com.example.ecommerce_mobile_app.util.CustomDialog;
import com.example.ecommerce_mobile_app.util.CustomToast;
import com.example.ecommerce_mobile_app.util.PrefManager;
import com.example.ecommerce_mobile_app.view.AddressShippingActivity;
import com.example.ecommerce_mobile_app.view.MainActivity;
import com.example.ecommerce_mobile_app.view.PlaceOrderActivity;
import com.example.ecommerce_mobile_app.view.ProductDetailActivity;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {

    FragmentCartBinding fragmentCartBinding;
    
    List<CartItem> mListCartItems;
    RecyclerView recyclerView;
    CartItemAdapter cartItemAdapter = new CartItemAdapter();

    private final InfoCart infoCart = new InfoCart();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCartBinding = FragmentCartBinding.inflate(inflater,container,false);
        recyclerView = fragmentCartBinding.rvListCart;
        setCart();

        fragmentCartBinding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Customer customer = new PrefManager(getContext()).getCustomer();
                if (customer.getAddressLine1() == null || customer.getAddressLine2() == null || customer.getCity() == null || customer.getCountry() == null)
                {
                    CustomDialog customDialog = new CustomDialog();
                    customDialog.setTitle("ADDRESS SHIPPING");
                    customDialog.setDes("Please fill full your address shipping");
                    customDialog.setTextPositive("OK");
                    customDialog.setPositiveButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            customDialog.dismiss();
                            Intent intent = new Intent(getContext(), AddressShippingActivity.class);
                            startActivity(intent);
                        }
                    });
                    customDialog.setNegativeButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            customDialog.dismiss();
                        }
                    });
                    customDialog.show(getActivity().getSupportFragmentManager(),"Fill address shipping");
                } else if (cartItemAdapter.getmListCartItems().size() == 0){
                    CustomDialog customDialog = new CustomDialog();
                    customDialog.setTitle("EMPTY CART");
                    customDialog.setDes("Your cart is empty. Please add something!");
                    customDialog.setTextPositive("Go store");
                    customDialog.setPositiveButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            customDialog.dismiss();
                            ((MainActivity) getActivity()).changeFragment(R.id.store);
                        }
                    });
                    customDialog.setNegativeButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            customDialog.dismiss();
                        }
                    });
                    customDialog.show(getActivity().getSupportFragmentManager(),"Empty cart");
                }
                else {
                    Intent intent = new Intent(getContext(), PlaceOrderActivity.class);
                    intent.putExtra("listCartItems", (Serializable) cartItemAdapter.getmListCartItems());
                    intent.putExtra("infoCart", (Serializable) infoCart);
                    startActivity(intent);
                }
            }
        });

        fragmentCartBinding.tvClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog customDialog = new CustomDialog();
                customDialog.setTitle("CLEAR CART");
                customDialog.setDes("Do you want to clear your cart?");
                customDialog.setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customDialog.dismiss();
                        clearCart();
                    }
                });
                customDialog.setNegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customDialog.dismiss();
                    }
                });
                customDialog.show(getActivity().getSupportFragmentManager(),"Clear cart");
            }
        });
        return fragmentCartBinding.getRoot();
    }

    public void setCart(){
        RetrofitClient.getInstance().getCart(new PrefManager(getContext()).getCustomer().getId()).enqueue(new Callback<BaseResponse<List<CartItem>>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<List<CartItem>>> call, Response<BaseResponse<List<CartItem>>> response) {
                if (response.isSuccessful()){

                    assert response.body() != null;
                    if (response.body().getResponse_message().equals("Success")){
                        mListCartItems = response.body().getData();
                        cartItemAdapter.setiClickOnCartItem(new CartItemAdapter.IClickOnCartItem() {
                            @Override
                            public void clickMinus(CartItem cartItem) {
                                if (cartItem.getQuantity() == 1){
                                    clickDelete(cartItemAdapter.getmListCartItems(),cartItem);
                                }else {
                                    updateCartItem(cartItem,"minus");
                                }
                            }

                            @Override
                            public void clickPlus(CartItem cartItem) {
                                updateCartItem(cartItem,"plus");
                            }

                            @Override
                            public void clickDelete(List<CartItem> mListCartItems, CartItem cartItem) {
                                CustomDialog customDialog = new CustomDialog();
                                customDialog.setPositiveButton(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        customDialog.dismiss();
                                        deleteCartItem(mListCartItems,cartItem);
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
    public void updateCartItem(CartItem cartItem, String action){
        RetrofitClient.getInstance().updateCartItem(new PrefManager(getContext()).getCustomer().getId(),cartItem.getProductId(),action.equals("minus") ? cartItem.getQuantity() - 1 : cartItem.getQuantity() + 1).enqueue(new Callback<BaseResponse<List<CartItem>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<CartItem>>> call, Response<BaseResponse<List<CartItem>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse_message().equals("Success")){
                        if (action.equals("plus")){
                            infoCart.setTotalPrice(infoCart.getTotalPrice() + cartItem.getSubtotal()/cartItem.getQuantity());
                            cartItem.setSubtotal(cartItem.getSubtotal() + cartItem.getSubtotal()/cartItem.getQuantity());
                            cartItem.setQuantity(cartItem.getQuantity() + 1);
                        }
                        if (action.equals("minus")){
                            infoCart.setTotalPrice(infoCart.getTotalPrice() - cartItem.getSubtotal()/cartItem.getQuantity());
                            cartItem.setSubtotal(cartItem.getSubtotal() - cartItem.getSubtotal()/cartItem.getQuantity());
                            cartItem.setQuantity(cartItem.getQuantity() - 1);
                        }

                    }
                    else {
                        CustomToast.showFailMessage(getContext(),response.body().getResponse_description());
                    }

                }
                else {
                    CustomToast.showFailMessage(getContext(),"Save cart is unsucessful!");
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<List<CartItem>>> call, Throwable t) {

            }
        });
    }
    public void deleteCartItem(List<CartItem> mListCartItems, CartItem cartItem){
        RetrofitClient.getInstance().removeCartItem(new PrefManager(getContext()).getCustomer().getId(),cartItem.getProductId()).enqueue(new Callback<BaseResponse<List<CartItem>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<CartItem>>> call, Response<BaseResponse<List<CartItem>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse_message().equals("Success")){
                        infoCart.setTotalPrice(infoCart.getTotalPrice() - cartItem.getSubtotal());
                        infoCart.setTotalItem(infoCart.getTotalItem() - 1);
                        mListCartItems.remove(cartItem);
                        cartItemAdapter.notifyDataSetChanged();
                        CustomToast.showSuccessMessage(getContext(),response.body().getResponse_description());
                    }
                    else {
                        CustomToast.showFailMessage(getContext(),response.body().getResponse_description());
                    }
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
    public void clearCart(){
        RetrofitClient.getInstance().deleteCart(new PrefManager(getContext()).getCustomer().getId()).enqueue(new Callback<BaseResponse<List<CartItem>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<CartItem>>> call, Response<BaseResponse<List<CartItem>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse_message().equals("Success")){
                        CustomToast.showSuccessMessage(getContext(),response.body().getResponse_description());
                        mListCartItems.clear();
                        cartItemAdapter.setmListCartItems(mListCartItems);
                        infoCart.setTotalItem(0);
                        infoCart.setTotalPrice(0.0f);
                    }else
                        CustomToast.showFailMessage(getContext(),response.body().getResponse_description());
                }
                else
                    CustomToast.showFailMessage(getContext(),"Clear cart is failure!");
            }

            @Override
            public void onFailure(Call<BaseResponse<List<CartItem>>> call, Throwable t) {

            }
        });
    }
}
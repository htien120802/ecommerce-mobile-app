package com.example.ecommerce_mobile_app.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ecommerce_mobile_app.model.Customer;
import com.google.gson.Gson;

public class PrefManager {
    Context context;
    Gson gson = new Gson();
    public PrefManager(Context context) {
        this.context = context;
    }

    public void saveLoginUser(Customer customer) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String customerJson = gson.toJson(customer);
        editor.putString("LogedCustomer",customerJson);
        editor.commit();
    }

    public Customer getCustomer() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        String customerJson = sharedPreferences.getString("LogedCustomer", "");
        Customer customer = new Gson().fromJson(customerJson,Customer.class);
        return customer;
    }

    public boolean isUserLogedOut() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        return sharedPreferences.getString("LogedCustomer", "").isEmpty();
    }

}

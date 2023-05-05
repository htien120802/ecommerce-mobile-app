package com.example.ecommerce_mobile_app.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ecommerce_mobile_app.model.Customer;

public class PrefManager {
    Context context;
    public PrefManager(Context context) {
        this.context = context;
    }

    public void saveLoginUser(String customer) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LogedCustomer",customer);
        editor.commit();
    }

    public String getCustomer() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        return sharedPreferences.getString("LogedCustomer", "");
    }

    public boolean isUserLogedOut() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        return sharedPreferences.getString("LogedCustomer", "").isEmpty();
    }

}

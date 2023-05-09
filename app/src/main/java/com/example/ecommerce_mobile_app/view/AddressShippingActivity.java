package com.example.ecommerce_mobile_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.databinding.ActivityAddressShippingBinding;
import com.example.ecommerce_mobile_app.model.Customer;
import com.example.ecommerce_mobile_app.util.PrefManager;

import java.util.prefs.PreferenceChangeEvent;

public class AddressShippingActivity extends AppCompatActivity {
    ActivityAddressShippingBinding activityAddressShippingBinding;
    PrefManager prefManager = new PrefManager(this);
    Customer customer;

    EditText addLine1, addLine2, city, country;
    Button cancel, edit_save;
    boolean nullAddress;
    boolean isEditting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddressShippingBinding = ActivityAddressShippingBinding.inflate(getLayoutInflater());
        setContentView(activityAddressShippingBinding.getRoot());
        customer = prefManager.getCustomer();
        activityAddressShippingBinding.setCustomer(customer);
        viewBinding();
        nullAddress = customer.getAddressLine1() == null || customer.getAddressLine2() == null || customer.getCity() == null || customer.getCountry() == null;
        if (nullAddress)
            edit_save.setText("Add");
        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEditting){
                    isEditting = true;
                    edit_save.setText("Save");
                    cancel.setVisibility(View.VISIBLE);
                    enableEditting(addLine1);
                    enableEditting(addLine2);
                    enableEditting(city);
                    enableEditting(country);
                }
                else {
                    isEditting = false;
                    nullAddress = false;
                    edit_save.setText("Edit");
                    cancel.setVisibility(View.GONE);

                    updateAddress();

                    disableEditting(addLine1);
                    disableEditting(addLine2);
                    disableEditting(city);
                    disableEditting(country);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditting = false;
                if (nullAddress)
                    edit_save.setText("Add");
                else
                    edit_save.setText("Edit");
                cancel.setVisibility(View.GONE);


                disableEditting(addLine1);
                disableEditting(addLine2);
                disableEditting(city);
                disableEditting(country);
            }
        });
        activityAddressShippingBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressShippingActivity.super.onBackPressed();
            }
        });
    }
    public void viewBinding(){
        addLine1 = activityAddressShippingBinding.etAddress1;
        addLine2 = activityAddressShippingBinding.etAddress2;
        city = activityAddressShippingBinding.etCityAddressShipping;
        country = activityAddressShippingBinding.etCountry;
        cancel = activityAddressShippingBinding.btnCancel;
        edit_save = activityAddressShippingBinding.btnEditSave;
    }
    public void enableEditting(EditText editText){
        editText.setFocusable(true);
        editText.setBackground(getDrawable(R.drawable.custom_input_profile_detail));
    }
    public void disableEditting(EditText editText){
        editText.setFocusable(false);
        editText.setBackground(null);
    }
    public void updateAddress(){

    }
}
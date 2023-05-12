package com.example.ecommerce_mobile_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.databinding.ActivityAddressShippingBinding;
import com.example.ecommerce_mobile_app.model.Customer;
import com.example.ecommerce_mobile_app.util.PrefManager;

import java.util.prefs.PreferenceChangeEvent;

public class AddressShippingActivity extends AppCompatActivity {
    ActivityAddressShippingBinding activityAddressShippingBinding;
    PrefManager prefManager = new PrefManager(this);
    Customer customer;
    // đây nha coi khúc này làm tiếp nè
    String[] listState =  {"1","2","3","4","5"};
    AutoCompleteTextView autoCompleteTxtState;
    ArrayAdapter<String> adapterState;
    // khúc này coi country rồi set theo api nha
    String[] listCountry =  {"Viet Nam","Thai Lan","Trung Quoc"};
    AutoCompleteTextView autoCompleteTxtCountry;
    ArrayAdapter<String> adapterCountry;


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
            }
        });
        activityAddressShippingBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressShippingActivity.super.onBackPressed();
            }
        });

        //khúc này là state mẫu nè nha
        autoCompleteTxtState = findViewById(R.id.auto_complete_txtState);
        adapterState = new ArrayAdapter<String>(this,R.layout.list_state,listState);
        autoCompleteTxtState.setAdapter(adapterState);
        autoCompleteTxtState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
            }
        });

        //khúc này là country mẫu nè nha
        autoCompleteTxtCountry = findViewById(R.id.auto_complete_txtCountry);
        adapterCountry = new ArrayAdapter<String>(this,R.layout.list_country,listCountry);
        autoCompleteTxtCountry.setAdapter(adapterCountry);
        autoCompleteTxtCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void viewBinding(){
        addLine1 = activityAddressShippingBinding.etAddress1;
        addLine2 = activityAddressShippingBinding.etAddress2;
        city = activityAddressShippingBinding.etCityAddressShipping;
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
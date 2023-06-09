package com.example.ecommerce_mobile_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.ActivityAddressShippingBinding;
import com.example.ecommerce_mobile_app.model.response.BaseResponse;
import com.example.ecommerce_mobile_app.model.Country;
import com.example.ecommerce_mobile_app.model.Customer;
import com.example.ecommerce_mobile_app.model.ShippingAddress;
import com.example.ecommerce_mobile_app.model.State;
import com.example.ecommerce_mobile_app.util.CustomToast;
import com.example.ecommerce_mobile_app.util.PrefManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressShippingActivity extends AppCompatActivity {
    ActivityAddressShippingBinding activityAddressShippingBinding;
    PrefManager prefManager = new PrefManager(this);
    Customer customer;
    // đây nha coi khúc này làm tiếp nè
    String[] listState;
    String[] listCountry;
    AutoCompleteTextView autoCompleteTxtCountry;
    AutoCompleteTextView autoCompleteTxtState;
    ArrayAdapter<String> adapterState;
    ArrayAdapter<String> adapterCountry;
    TextInputLayout textInputLayoutState, textInputLayoutCoutry;
    Map<Integer,String> mapCountry = new HashMap<Integer,String>();
    Map<Integer,String> mapState = new HashMap<Integer,String>();
    String conutry, state;
    TextView tvState, tvCountry;
    EditText addLine1, addLine2, city, postalCode;
    Button cancel, edit_save;
    boolean nullAddress;
    boolean isEditting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddressShippingBinding = ActivityAddressShippingBinding.inflate(getLayoutInflater());
        setContentView(activityAddressShippingBinding.getRoot());

        customer = prefManager.getCustomer();
        conutry = customer.getCountry() != null ? customer.getCountry().getName() : "";
        state = customer.getState();

        activityAddressShippingBinding.setCustomer(customer);

        viewBinding();

        nullAddress = customer.getAddressLine1() == null || customer.getAddressLine2() == null || customer.getCity() == null || customer.getCountry() == null;
        if (nullAddress)
            edit_save.setText("Add");
//        if (customer.getCountry() == null)
//            customer.setCountry(new Country(-1,""));

        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEditting){
                    isEditting = true;
                    edit_save.setText("Save");
                    cancel.setVisibility(View.VISIBLE);

                    textInputLayoutCoutry.setVisibility(View.VISIBLE);
                    textInputLayoutState.setVisibility(View.VISIBLE);
                    tvCountry.setVisibility(View.GONE);
                    tvState.setVisibility(View.GONE);


                    enableEditting(addLine1);
                    enableEditting(addLine2);
                    enableEditting(city);
                    enableEditting(postalCode);

                    getCountry();

                }
                else {
                    updateAddress();
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

                textInputLayoutCoutry.setVisibility(View.GONE);
                textInputLayoutState.setVisibility(View.GONE);
                tvCountry.setVisibility(View.VISIBLE);
                tvState.setVisibility(View.VISIBLE);
                activityAddressShippingBinding.LayoutAddress4.setVisibility(View.VISIBLE);

                disableEditting(addLine1);
                disableEditting(addLine2);
                disableEditting(city);
                disableEditting(postalCode);

                showPreviousInfo();
            }
        });
        activityAddressShippingBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressShippingActivity.super.onBackPressed();
            }
        });


        autoCompleteTxtCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                conutry = parent.getItemAtPosition(position).toString();
                getStateByCountry(conutry);
            }
        });
        autoCompleteTxtState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                state = parent.getItemAtPosition(position).toString();
            }
        });




    }
    public void viewBinding(){
        addLine1 = activityAddressShippingBinding.etAddress1;
        addLine2 = activityAddressShippingBinding.etAddress2;
        city = activityAddressShippingBinding.etCityAddressShipping;
        postalCode = activityAddressShippingBinding.etPostalCode;
        tvState = activityAddressShippingBinding.tvShowState;
        tvCountry = activityAddressShippingBinding.tvShowCountry;
        cancel = activityAddressShippingBinding.btnCancel;
        edit_save = activityAddressShippingBinding.btnEditSave;
        //
        textInputLayoutState = activityAddressShippingBinding.textinputLayoutState;
        textInputLayoutCoutry = activityAddressShippingBinding.textinputLayoutCountry;
        autoCompleteTxtState = activityAddressShippingBinding.autoCompleteTxtState;
        autoCompleteTxtCountry = activityAddressShippingBinding.autoCompleteTxtCountry;
    }
    public void enableEditting(EditText editText){
        editText.setEnabled(true);
        editText.setBackground(getDrawable(R.drawable.custom_input_profile_detail));
    }
    public void disableEditting(EditText editText){
        editText.setEnabled(false);
        editText.setBackground(null);
    }
    public void getCountry(){
        RetrofitClient.getInstance().getCountry().enqueue(new Callback<BaseResponse<List<Country>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Country>>> call, Response<BaseResponse<List<Country>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse_message().equals("Success")){
                        List<Country> countries = response.body().getData();
                        listCountry =  new String[countries.size()];

                        mapCountry.clear();
                        for (int i = 0; i< countries.size(); i++ ){
                            mapCountry.put(countries.get(i).getId(),countries.get(i).getName());
                            listCountry[i] = countries.get(i).getName();
                        }
                        adapterCountry = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_country_state,listCountry);
                        autoCompleteTxtCountry.setAdapter(adapterCountry);
                        autoCompleteTxtCountry.setText(conutry,false);
                        getStateByCountry(conutry);
                    }
                    else {
                        CustomToast.showFailMessage(getApplicationContext(),response.body().getResponse_description());
                    }
                }
                else {
                    CustomToast.showFailMessage(getApplicationContext(),"Show country is failure");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Country>>> call, Throwable t) {

            }
        });
    }
    public void getStateByCountry(String country){
        int id = -1;
        for (Map.Entry<Integer,String> entry : mapCountry.entrySet()){
            if (entry.getValue().equals(country)){
                id = entry.getKey();
                break;
            }
        }
        RetrofitClient.getInstance().getStateByCountry(id).enqueue(new Callback<BaseResponse<List<State>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<State>>> call, Response<BaseResponse<List<State>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse_message().equals("Success")){
                        List<State> states = response.body().getData();
                        listState = new String[states.size()];
                        mapState.clear();
                        for (int  i=0; i<states.size(); i++){
                            mapState.put(states.get(i).getId(),states.get(i).getName());
                            listState[i] = states.get(i).getName();
                        }
                        adapterState =  new ArrayAdapter<String>(getApplicationContext(),R.layout.list_country_state,listState);
                        autoCompleteTxtState.setAdapter(adapterState);
                        if (states.size() != 0){
                            activityAddressShippingBinding.LayoutAddress4.setVisibility(View.VISIBLE);
                            autoCompleteTxtState.setText(state,false);
                        }
                        else {
                            activityAddressShippingBinding.LayoutAddress4.setVisibility(View.GONE);
                        }
                    }
                    else {
                        CustomToast.showFailMessage(getApplicationContext(),response.body().getResponse_description());
                    }
                }
                else {
                    CustomToast.showFailMessage(getApplicationContext(),"Show state is failure!");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<State>>> call, Throwable t) {

            }
        });
    }
    private int findIdByCountryName(String conutry){
        for (Map.Entry<Integer,String> entry : mapCountry.entrySet()){
            if (entry.getValue().equals(conutry)){
                return entry.getKey();
            }
        }
        return -1;
    }
    private int findIdByStateName(String state){
        for (Map.Entry<Integer,String> entry : mapState.entrySet()){
            if (entry.getValue().equals(state)){
                return entry.getKey();
            }
        }
        return -1;
    }
    public void updateAddress(){
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddressLine1(addLine1.getText().toString());
        shippingAddress.setAddressLine2(addLine2.getText().toString());
        shippingAddress.setCity(city.getText().toString());
        shippingAddress.setPostalCode(postalCode.getText().toString());
        shippingAddress.setCountryId(findIdByCountryName(conutry));
        shippingAddress.setStateId(findIdByStateName(state));
        Log.e("ERR",shippingAddress.getAddressLine1() + ", " + shippingAddress.getAddressLine2() + ", " + shippingAddress.getCity() + ", "+ shippingAddress.getPostalCode() + ", " + shippingAddress.getCountryId() + ", " + shippingAddress.getStateId());
        RetrofitClient.getInstance().updateAddress(customer.getId(), shippingAddress).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse_message().equals("Success")){
                        CustomToast.showSuccessMessage(getApplicationContext(),response.body().getResponse_description());
                        isEditting = false;
                        nullAddress = false;
                        edit_save.setText("Edit");
                        cancel.setVisibility(View.GONE);

                        textInputLayoutCoutry.setVisibility(View.GONE);
                        textInputLayoutState.setVisibility(View.GONE);
                        tvCountry.setVisibility(View.VISIBLE);
                        tvState.setVisibility(View.VISIBLE);

                        disableEditting(addLine1);
                        disableEditting(addLine2);
                        disableEditting(city);
                        disableEditting(postalCode);

                        customer.setAddressLine1(shippingAddress.getAddressLine1());
                        customer.setAddressLine2(shippingAddress.getAddressLine2());
                        customer.setPostalCode(shippingAddress.getPostalCode());
                        customer.setCity(shippingAddress.getCity());
                        customer.setCountry(new Country(shippingAddress.getCountryId(),conutry));
                        customer.setState(state);
                        prefManager.changeCustomer(customer);
                    }
                    else {
                        CustomToast.showFailMessage(getApplicationContext(),response.body().getResponse_description());
                    }
                }
                else {
                    CustomToast.showFailMessage(getApplicationContext(),"Update your address is failure");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

            }
        });
    }
    private void showPreviousInfo(){
        addLine1.setText(customer.getAddressLine1());
        addLine2.setText(customer.getAddressLine2());
        city.setText(customer.getCity());
        postalCode.setText(customer.getPostalCode());
        conutry = customer.getCountry() != null ? customer.getCountry().getName() : "";
        autoCompleteTxtCountry.setText(conutry,false);
        state = customer.getState();
        autoCompleteTxtState.setText(state,false);
    }
}
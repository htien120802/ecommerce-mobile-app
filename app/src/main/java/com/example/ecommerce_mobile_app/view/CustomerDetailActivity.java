package com.example.ecommerce_mobile_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.ActivityPersonalDetailsBinding;
import com.example.ecommerce_mobile_app.model.BaseResponse;
import com.example.ecommerce_mobile_app.model.Customer;
import com.example.ecommerce_mobile_app.model.Profile;
import com.example.ecommerce_mobile_app.util.CustomToast;
import com.example.ecommerce_mobile_app.util.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerDetailActivity extends AppCompatActivity {
    ActivityPersonalDetailsBinding activityPersonalDetailsBinding;
    PrefManager prefManager = new PrefManager(this);
    Customer customer;
    EditText lastName, firstName, username, email, phoneNumber;
    boolean isEditting = false;
    Button cancel, edit_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPersonalDetailsBinding = ActivityPersonalDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityPersonalDetailsBinding.getRoot());

        viewBinding();
        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEditting){
                    isEditting = true;
                    edit_save.setText("Save");
                    cancel.setVisibility(View.VISIBLE);
                    enableEditting(lastName);
                    enableEditting(firstName);
                    enableEditting(username);
                    enableEditting(email);
                    enableEditting(phoneNumber);
                }
                else{

                    Profile profile = new Profile();
                    profile.setFirstName(firstName.getText().toString());
                    profile.setLastName(lastName.getText().toString());
                    profile.setUsername(username.getText().toString());
                    profile.setEmail(email.getText().toString());
                    profile.setPhoneNumber(phoneNumber.getText().toString());

                    updateProfile(profile);



                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditting = false;
                edit_save.setText("Edit");
                cancel.setVisibility(View.GONE);
                disableEditting(lastName);
                disableEditting(firstName);
                disableEditting(username);
                disableEditting(email);
                disableEditting(phoneNumber);
            }
        });
        customer = prefManager.getCustomer();
        activityPersonalDetailsBinding.setCustomer(customer);
        activityPersonalDetailsBinding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerDetailActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("change_to","profile");
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
    public void viewBinding(){
        lastName = activityPersonalDetailsBinding.etLastNameProfileDetail;
        firstName = activityPersonalDetailsBinding.etFirstNameProfileDetail;
        username = activityPersonalDetailsBinding.etUserNameProfileDetail;
        email = activityPersonalDetailsBinding.etEmailProfileDetail;
        phoneNumber = activityPersonalDetailsBinding.etPhoneNumberProfileDetail;
        cancel = activityPersonalDetailsBinding.btnCancel;
        edit_save = activityPersonalDetailsBinding.btnEditSaveProfileDetail;
    }
    public void enableEditting(EditText editText){
        editText.setEnabled(true);
        editText.setBackground(getDrawable(R.drawable.custom_input_profile_detail));
    }
    public void disableEditting(EditText editText){
        editText.setEnabled(false);
        editText.setBackground(null);
    }
    public void updateProfile(Profile profile){
        RetrofitClient.getInstance().updateInfo(customer.getId(),profile).enqueue(new Callback<BaseResponse<Customer>>() {
            @Override
            public void onResponse(Call<BaseResponse<Customer>> call, Response<BaseResponse<Customer>> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse_message().equals("Update Success")){
                        CustomToast.showSuccessMessage(getApplicationContext(),response.body().getResponse_description());
                        isEditting = false;
                        edit_save.setText("Edit");
                        cancel.setVisibility(View.GONE);
                        disableEditting(lastName);
                        disableEditting(firstName);
                        disableEditting(username);
                        disableEditting(email);
                        disableEditting(phoneNumber);
                        customer.setFirstName(profile.getFirstName());
                        customer.setLastName(profile.getLastName());
                        customer.setUsername(profile.getUsername());
                        customer.setEmail(profile.getEmail());
                        customer.setPhoneNumber(profile.getPhoneNumber());
                        prefManager.changeCustomer(customer);
                    }
                    else {
                        CustomToast.showFailMessage(getApplicationContext(),response.body().getResponse_description());
                    }
                }
                else {
                    CustomToast.showFailMessage(getApplicationContext(),"Update profile is failure. Please try again!");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Customer>> call, Throwable t) {

            }
        });
    }
}
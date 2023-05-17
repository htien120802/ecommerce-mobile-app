package com.example.ecommerce_mobile_app.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.ActivityPersonalDetailsBinding;
import com.example.ecommerce_mobile_app.model.response.BaseResponse;
import com.example.ecommerce_mobile_app.model.Customer;
import com.example.ecommerce_mobile_app.model.Profile;
import com.example.ecommerce_mobile_app.util.CustomToast;
import com.example.ecommerce_mobile_app.util.PrefManager;
import com.example.ecommerce_mobile_app.util.RealPathUtil;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerDetailActivity extends AppCompatActivity {
    ActivityPersonalDetailsBinding activityPersonalDetailsBinding;
    PrefManager prefManager = new PrefManager(this);
    Customer customer;
    EditText lastName, firstName, username, email, phoneNumber;
    boolean isEditting = false;
    Button cancel, edit_save, cancel_upload, save_upload;
    private ProgressDialog mProgressDialog;
    public static final int MY_REQUEST_CODE=100;
    private Uri mUri;
    private ShapeableImageView avatar;
    public static final String TAG = CustomerDetailActivity.class.getName();

    boolean isUpdated;
    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
//                    Log.e(TAG, "onActivityResult");
                    if(result.getResultCode()== Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        Uri uri = data.getData();
                        System.out.println(uri.toString());
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            avatar.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPersonalDetailsBinding = ActivityPersonalDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityPersonalDetailsBinding.getRoot());
        customer = prefManager.getCustomer();
        activityPersonalDetailsBinding.setCustomer(customer);
        isUpdated = false;
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
        activityPersonalDetailsBinding.btnUpdateImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    CheckPermission();
                    cancel_upload.setVisibility(View.VISIBLE);
                    save_upload.setVisibility(View.VISIBLE);
            }
        });
        cancel_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_upload.setVisibility(View.GONE);
                save_upload.setVisibility(View.GONE);
                MainActivity.setImage(avatar,customer.getPhotoImagePath());
            }
        });

        save_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        activityPersonalDetailsBinding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpdated){
                    Intent intent = new Intent(CustomerDetailActivity.this,MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("change_to","profile");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                else {
                    CustomerDetailActivity.super.onBackPressed();
                }
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
        cancel_upload = activityPersonalDetailsBinding.btnCancelImage;
        save_upload  = activityPersonalDetailsBinding.btnUploadImage;
        avatar = activityPersonalDetailsBinding.shapeableImageView6;
        mProgressDialog = new ProgressDialog((CustomerDetailActivity.this));
        mProgressDialog.setMessage("Please wait upload....");
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
                    if (response.body().getResponse_message().equals("Success")){
                        isUpdated = true;
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
    private void CheckPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openGallery();
            return;
        }
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
        }else {
            String[] permission = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission,MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == MY_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent,"Select picture"));
    }
    public void uploadImage(){
        mProgressDialog.show();
        String IMAGE_PATH = RealPathUtil.getRealPath(this,mUri);
        File f = new File(IMAGE_PATH);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        MultipartBody.Part file = MultipartBody.Part.createFormData("file", f.getName(), requestFile);

        RetrofitClient.getInstance().updatePhoto(customer.getId(),file).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()){
                    mProgressDialog.dismiss();
                    isUpdated = true;
                    if (response.body().getResponse_message().equals("Success")){
                        CustomToast.showSuccessMessage(getApplicationContext(),response.body().getResponse_description());
                        MainActivity.setImage(avatar,response.body().getData());
                        cancel_upload.setVisibility(View.GONE);
                        save_upload.setVisibility(View.GONE);
                        customer.setPhotoImagePath(response.body().getData());
                        prefManager.changeCustomer(customer);
                    }
                    else {
                        CustomToast.showFailMessage(getApplicationContext(),response.body().getResponse_description());
                    }
                }
                else {
                    mProgressDialog.dismiss();
                    CustomToast.showFailMessage(getApplicationContext(),"Upload image is failure. Please try again!");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

            }
        });
    }
}
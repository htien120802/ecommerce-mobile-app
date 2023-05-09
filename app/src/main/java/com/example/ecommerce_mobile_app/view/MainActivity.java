package com.example.ecommerce_mobile_app.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.api.CONSTANT;
import com.example.ecommerce_mobile_app.databinding.ActivityMainBinding;
import com.example.ecommerce_mobile_app.model.Customer;
import com.example.ecommerce_mobile_app.util.PrefManager;
import com.example.ecommerce_mobile_app.view.fragment.CartFragment;
import com.example.ecommerce_mobile_app.view.fragment.HomeFragment;
import com.example.ecommerce_mobile_app.view.fragment.ProfileFragment;
import com.example.ecommerce_mobile_app.view.fragment.StoreFragment;
import com.google.gson.Gson;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    Fragment fragment=null;
    ChipNavigationBar chipNavigationBar;
    String change_to = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Customer customer = new PrefManager(this).getCustomer();
        if (customer != null){
            activityMainBinding.tvNameUser.setText(customer.getLastName());
            setImage(activityMainBinding.imgAvatar,customer.getPhotoImagePath());
        }

        chipNavigationBar = activityMainBinding.NavigationBar;
        chipNavigationBar.setItemSelected(R.id.home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            change_to = bundle.getSerializable("change_to").toString();
            if (change_to.equals("cart"))
                changeFragment(R.id.cart);
            if (change_to.equals("profile"))
                changeFragment(R.id.profile);
        }
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                changeFragment(i);
            }
        });
    }
    public void changeFragment(int i){
        switch (i)
        {
            case R.id.home:
                fragment = new HomeFragment();
                break;
            case R.id.store:
                fragment = new StoreFragment();
                break;
            case R.id.cart:
                fragment = new CartFragment();
                break;
            case R.id.profile:
                fragment = new ProfileFragment();
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            chipNavigationBar.setItemSelected(i,true);
        }
    }
    @BindingAdapter("setImage")
    public static void setImage(ImageView shapeableImageView, String imagePath){
        imagePath = CONSTANT.BASE_URL + imagePath.substring(1,imagePath.length());
        Glide.with(shapeableImageView.getContext()).load(imagePath).error(shapeableImageView.getContext().getDrawable(R.drawable.avatarhome1)).into(shapeableImageView);
    }
}
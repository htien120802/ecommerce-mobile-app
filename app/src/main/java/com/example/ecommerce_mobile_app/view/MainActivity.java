package com.example.ecommerce_mobile_app.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.StrictMode;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.databinding.ActivityMainBinding;
import com.example.ecommerce_mobile_app.view.fragment.CartFragment;
import com.example.ecommerce_mobile_app.view.fragment.HomeFragment;
import com.example.ecommerce_mobile_app.view.fragment.StoreFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    Fragment fragment=null;
    ChipNavigationBar chipNavigationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        chipNavigationBar = activityMainBinding.NavigationBar;
        chipNavigationBar.setItemSelected(R.id.home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
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
                }

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                }
            }
        });
    }
}
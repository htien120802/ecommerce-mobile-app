package com.example.ecommerce_mobile_app.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.databinding.FragmentProfileBinding;
import com.example.ecommerce_mobile_app.util.PrefManager;
import com.example.ecommerce_mobile_app.view.AddressShippingActivity;
import com.example.ecommerce_mobile_app.view.CustomerDetailActivity;
import com.example.ecommerce_mobile_app.view.SignInActivity;


public class ProfileFragment extends Fragment {
    FragmentProfileBinding fragmentProfileBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater,container,false);
        fragmentProfileBinding.LayoutUserDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CustomerDetailActivity.class);
                startActivity(intent);
            }
        });

        fragmentProfileBinding.LayoutAddressShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddressShippingActivity.class);
                startActivity(intent);
            }
        });

        fragmentProfileBinding.btnLogoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PrefManager(getContext()).removeCustomer();
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return fragmentProfileBinding.getRoot();
    }
}
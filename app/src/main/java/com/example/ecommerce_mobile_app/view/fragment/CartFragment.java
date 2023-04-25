package com.example.ecommerce_mobile_app.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.databinding.FragmentCartBinding;


public class CartFragment extends Fragment {

    FragmentCartBinding fragmentCartBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCartBinding = FragmentCartBinding.inflate(inflater,container,false);

        return fragmentCartBinding.getRoot();
    }
}
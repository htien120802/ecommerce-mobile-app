package com.example.ecommerce_mobile_app.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {
    FragmentHomeBinding fragmentHomeBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater,container,false);

        return fragmentHomeBinding.getRoot();
    }
}
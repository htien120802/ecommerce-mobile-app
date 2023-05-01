package com.example.ecommerce_mobile_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.api.CONSTANT;
import com.example.ecommerce_mobile_app.databinding.ListItemBinding;
import com.example.ecommerce_mobile_app.model.Product;
import com.example.ecommerce_mobile_app.view.ProductDetailActivity;


import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private List<Product> mListProducts;
    private List<Product> mOldListProducts;
    private boolean keySearch = false;
    private boolean categoryFilter = false;
    private Context context;
    public ProductAdapter(List<Product> mListProducts, Context context) {
        this.mListProducts = mListProducts;
        this.context = context;
        this.mOldListProducts = mListProducts;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemBinding listItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.list_item,parent,false);
            return new ProductViewHolder(listItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mListProducts.get(position);
        holder.listItemBinding.setProduct(product);
        holder.listItemBinding.LayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                Log.e("CLICK",product.getId().toString());
                bundle.putSerializable("product_id",product.getId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListProducts != null ? mListProducts.size() : 0;
    }



    public void doFilter(String categoryName, String keySearch){
        List<Product> products =  new ArrayList<>();
        if (categoryName.equals("All") && keySearch.equals("")){
            products = mOldListProducts;
        }
        if (categoryName.equals("All") && !keySearch.equals("")){
            for (Product product : mOldListProducts)
                if (product.getName().toLowerCase().contains(keySearch.toLowerCase()))
                    products.add(product);
        }
        if (!categoryName.equals("All") && keySearch.equals("")){
            for (Product product : mOldListProducts)
                if (product.getCategory().getName().equals(categoryName))
                    products.add(product);

        }
        if (!categoryName.equals("All") && !keySearch.equals("")){
            for (Product product : mOldListProducts)
                if (product.getName().toLowerCase().contains(keySearch.toLowerCase()) && product.getCategory().getName().equals(categoryName))
                    products.add(product);
        }
        mListProducts = products;
        notifyDataSetChanged();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        private ListItemBinding listItemBinding;

        public ProductViewHolder(@NonNull ListItemBinding listItemBinding) {
            super(listItemBinding.getRoot());
            this.listItemBinding = listItemBinding;
        }

    }
}

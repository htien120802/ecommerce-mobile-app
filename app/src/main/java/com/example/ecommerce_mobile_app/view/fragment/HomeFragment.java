package com.example.ecommerce_mobile_app.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.adapter.BoxProductAdapter;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.databinding.FragmentHomeBinding;
import com.example.ecommerce_mobile_app.model.BaseResponse;
import com.example.ecommerce_mobile_app.model.Product;
import com.example.ecommerce_mobile_app.model.WishlistItem;
import com.example.ecommerce_mobile_app.util.PrefManager;
import com.example.ecommerce_mobile_app.view.MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    FragmentHomeBinding fragmentHomeBinding;
    RecyclerView rcvNew, rcvPopular;
    BoxProductAdapter adapterNew = new BoxProductAdapter(), adapterPopular = new BoxProductAdapter();
    List<Product> mListProducts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater,container,false);

        setListProducts();
        getListFavProduct();
        fragmentHomeBinding.tvViewAllNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(R.id.store);
            }
        });

        fragmentHomeBinding.tvViewAllPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(R.id.store);
            }
        });
        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setListProducts(){
        rcvNew = fragmentHomeBinding.rvNewArrivalsHome;
        rcvPopular = fragmentHomeBinding.rvPopularHome;
        RetrofitClient.getInstance().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()){
                    mListProducts = response.body();

                    List<Product> popularProducts = mListProducts.subList(0,4);
                    List<Product> temp = mListProducts.subList(4,mListProducts.size());
                    for (int i = 0; i < temp.size(); i++ )
                        for (int j = 0; j < popularProducts.size(); j++)
                            if (temp.get(i).getReviewCount() > popularProducts.get(j).getReviewCount()){
                                popularProducts.set(j,temp.get(i));
                                break;
                            }

                    adapterPopular.setmListProducts(popularProducts);
                    adapterPopular.setContext(getContext());
                    GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(),2);
                    rcvPopular.setLayoutManager(gridLayoutManager1);
                    rcvPopular.setAdapter(adapterPopular);

                    List<Product> newProducts = mListProducts.subList(mListProducts.size()-4,mListProducts.size());
                    adapterNew.setmListProducts(newProducts);
                    adapterNew.setContext(getContext());
                    GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(),2);
                    rcvNew.setLayoutManager(gridLayoutManager2);
                    rcvNew.setAdapter(adapterNew);
                }

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });


    }
    public void getListFavProduct(){
        RetrofitClient.getInstance().getWishlist(new PrefManager(getContext()).getCustomer().getId()).enqueue(new Callback<BaseResponse<List<WishlistItem>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<WishlistItem>>> call, Response<BaseResponse<List<WishlistItem>>> response) {
                if (response.isSuccessful() && response.body().getResponse_message().equals("Success")){
                    adapterNew.setmListFavProducts(response.body().getData());
                    adapterPopular.setmListFavProducts(response.body().getData());
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<List<WishlistItem>>> call, Throwable t) {

            }
        });
    }

}
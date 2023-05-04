package com.example.ecommerce_mobile_app.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_mobile_app.adapter.ProductAdapter;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.model.Product;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SetHomeProductAsyncTask extends AsyncTask<Void, Void, List<Product>> {
    RecyclerView rcvNew, rcvPopular;
    ProductAdapter adapterNew, adapterPopular;
    Context context;

    public SetHomeProductAsyncTask(RecyclerView rcvNew, RecyclerView rcvPopular, ProductAdapter adapterNew, ProductAdapter adapterPopular, Context context) {
        this.rcvNew = rcvNew;
        this.rcvPopular = rcvPopular;
        this.adapterNew = adapterNew;
        this.adapterPopular = adapterPopular;
        this.context = context;
    }

    @Override
    protected List<Product> doInBackground(Void... voids) {
        Call<List<Product>> call = RetrofitClient.getInstance().getProducts();
        try {
            Response<List<Product>> response = call.execute();
            if (response.isSuccessful()){
                return response.body();
            }else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void onPostExecute(List<Product> products) {
        if (products != null){
            List<Product> popularProducts = products.subList(0,4);
            List<Product> temp = products.subList(4,products.size());
            for (int i = 0; i < temp.size(); i++ )
                for (int j = 0; j < popularProducts.size(); j++)
                    if (temp.get(i).getReviewCount() > popularProducts.get(j).getReviewCount()){
                        popularProducts.set(j,temp.get(i));
                        break;
                    }
            adapterPopular.setmListProducts(popularProducts);
            adapterPopular.setContext(context);
            GridLayoutManager gridLayoutManager1 = new GridLayoutManager(context,2);
            rcvPopular.setLayoutManager(gridLayoutManager1);
            rcvPopular.setAdapter(adapterPopular);

            List<Product> newProducts = products.subList(products.size()-4,products.size());
            adapterNew.setmListProducts(newProducts);
            adapterNew.setContext(context);
            GridLayoutManager gridLayoutManager2 = new GridLayoutManager(context,2);
            rcvNew.setLayoutManager(gridLayoutManager2);
            rcvNew.setAdapter(adapterNew);
        }
    }
}

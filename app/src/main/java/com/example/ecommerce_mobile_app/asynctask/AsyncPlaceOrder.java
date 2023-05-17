package com.example.ecommerce_mobile_app.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.model.BaseResponse;
import com.example.ecommerce_mobile_app.util.CustomToast;
import com.example.ecommerce_mobile_app.util.OrderSuccessDialog;
import com.example.ecommerce_mobile_app.util.PrefManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class AsyncPlaceOrder extends AsyncTask<Void, Void, BaseResponse<String>> {
    private Context context;
    private doOnResponse doOnResponse;
    public interface doOnResponse {
        public void onSuccess(BaseResponse<String> stringBaseResponse);
        public void onFailure(BaseResponse<String> stringBaseResponse);
    }

    public AsyncPlaceOrder(Context context, doOnResponse doOnResponse) {
        this.context = context;
        this.doOnResponse = doOnResponse;
    }

    @Override
    protected BaseResponse<String> doInBackground(Void... voids) {
        Call<BaseResponse<String>> call = RetrofitClient.getInstance().placeOrder(new PrefManager(context).getCustomer().getId());
        try {
            Response<BaseResponse<String>> response = call.execute();
            if (response.isSuccessful())
                return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(BaseResponse<String> stringBaseResponse) {
        if (stringBaseResponse.getResponse_message().equals("Success"))
        {
            doOnResponse.onSuccess(stringBaseResponse);
        }
        else {
            doOnResponse.onFailure(stringBaseResponse);
        }
    }
}

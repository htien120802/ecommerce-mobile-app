package com.example.ecommerce_mobile_app.AsyncTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.model.Customer;
import com.example.ecommerce_mobile_app.model.SignInRequest;
import com.example.ecommerce_mobile_app.model.SignInResponse;
import com.example.ecommerce_mobile_app.view.MainActivity;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignInAsyncTask extends AsyncTask<Void, Void, SignInResponse> {
    private Context context;
    private SignInRequest signInRequest;

    public SignInAsyncTask(Context context, SignInRequest signInRequest) {
        this.context = context;
        this.signInRequest = signInRequest;
    }

    @Override
    protected SignInResponse doInBackground(Void... voids) {
//        JsonReader.setLenient(context,true);
        Call<SignInResponse> call = RetrofitClient.getInstance().signIn(signInRequest);
        try {
            Response<SignInResponse> response = call.execute();
            if (response.isSuccessful()){
                return response.body();
            }
            else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(SignInResponse s) {
        if (s!=null){
            if (s.getData() != null){
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            else {
                Toast.makeText(context,s.getResponse_description(),Toast.LENGTH_SHORT).show();
            }
        }
    }
}

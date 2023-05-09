package com.example.ecommerce_mobile_app.api;

import com.example.ecommerce_mobile_app.model.CartItem;
import com.example.ecommerce_mobile_app.model.Category;
import com.example.ecommerce_mobile_app.model.Customer;
import com.example.ecommerce_mobile_app.model.Product;
import com.example.ecommerce_mobile_app.model.Profile;
import com.example.ecommerce_mobile_app.model.SignInRequest;
import com.example.ecommerce_mobile_app.model.BaseResponse;
import com.example.ecommerce_mobile_app.model.SignUpRequest;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIService {
//    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
//    APIService apiService = new Retrofit.Builder()
//            .baseUrl("http://192.168.1.13:8081/")
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//            .create(APIService.class);
    @GET("api/categories")
    Call<List<Category>> getCategories();

    @GET("api/products")
    Call<List<Product>> getProducts();

    @GET("api/products/{id}")
    Call<Product> getProductById(@Path("id") int id);

    @POST("api/auth/signin")
    Call<BaseResponse<Customer>> signIn(@Body SignInRequest signInRequest);

    @POST("api/auth/signup")
    Call<BaseResponse<Customer>> signUp(@Body SignUpRequest signUpRequest);

    @GET("api/cart/{customerId}")
    Call<BaseResponse<List<CartItem>>> getCart(@Path("customerId") int customerId);

    @POST("api/cart/{customerId}/update/{productId}/{quantity}")
    Call<BaseResponse<List<CartItem>>> updateCartItem(@Path("customerId") int customerId, @Path("productId") int productId, @Path("quantity") int quantity);

    @DELETE("api/cart/{customerId}/remove/{productId}")
    Call<BaseResponse<List<CartItem>>> removeCartItem(@Path("customerId") int customerId, @Path("productId") int productId);

    @POST("api/cart/{customerId}/add/{productId}/{quantity}")
    Call<BaseResponse<List<CartItem>>> addCartItem(@Path("customerId") int customerId, @Path("productId") int productId, @Path("quantity") int quantity);

    @POST("api/account/{id}/update_info")
    Call<BaseResponse<Customer>> updateInfo(@Path("id") int id, @Body Profile profile);
    @Multipart
    @POST("api/account/{id}/update_photo")
    Call<BaseResponse<String>> updatePhoto(@Path("id") int id, @Part MultipartBody.Part image);
}

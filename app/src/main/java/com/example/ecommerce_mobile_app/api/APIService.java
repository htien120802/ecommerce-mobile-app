package com.example.ecommerce_mobile_app.api;

import com.example.ecommerce_mobile_app.model.BaseResponse;
import com.example.ecommerce_mobile_app.model.CartItem;
import com.example.ecommerce_mobile_app.model.Category;
import com.example.ecommerce_mobile_app.model.Country;
import com.example.ecommerce_mobile_app.model.Customer;
import com.example.ecommerce_mobile_app.model.Order;
import com.example.ecommerce_mobile_app.model.Product;
import com.example.ecommerce_mobile_app.model.Profile;
import com.example.ecommerce_mobile_app.model.Question;
import com.example.ecommerce_mobile_app.model.ShippingAddress;
import com.example.ecommerce_mobile_app.model.SignInRequest;
import com.example.ecommerce_mobile_app.model.SignUpRequest;
import com.example.ecommerce_mobile_app.model.State;
import com.example.ecommerce_mobile_app.model.UpdatePasswordRequest;
import com.example.ecommerce_mobile_app.model.WishlistItem;

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
    @GET("api/category")
    Call<List<Category>> getCategories();

    @GET("api/product")
    Call<List<Product>> getProducts();

    @GET("api/product/{id}")
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
    Call<BaseResponse<String>> updatePhoto(@Path("id") int id, @Part MultipartBody.Part file);

    @POST("api/account/{id}/update_password")
    Call<BaseResponse<String>> updatePassword(@Path("id") int id, @Body UpdatePasswordRequest updatePasswordRequest);

    @POST("api/account/{id}/update_address")
    Call<BaseResponse<String>> updateAddress(@Path("id") int id, @Body ShippingAddress shippingAddress);
    @GET("api/country")
    Call<BaseResponse<List<Country>>> getCountry();

    @GET("api/list_states_by_country/{id}")
    Call<BaseResponse<List<State>>> getStateByCountry(@Path("id") int id);

    @GET("api/account/{customerId}/order")
    Call<BaseResponse<List<Order>>> getOrders(@Path("customerId") int customerId);

    @GET("api/wishlist/{customerId}")
    Call<BaseResponse<List<WishlistItem>>> getWishlist(@Path("customerId") int customerId);

    @POST("api/wishlist/{customerId}/add/{productId}")
    Call<BaseResponse<List<WishlistItem>>> addWishlistItem(@Path("customerId") int customerId, @Path("productId") int productId);

    @DELETE("api/cart/{customerId}/remove/{productId}")
    Call<BaseResponse<List<WishlistItem>>> removeWishlistItem(@Path("customerId") int customerId, @Path("productId") int productId);

    @DELETE("api/cart/{customerId}/delete/{productId}")
    Call<BaseResponse<List<WishlistItem>>> deleteWishlist(@Path("customerId") int customerId);

    @GET("api/list_questions_by_product/{productId}")
    Call<BaseResponse<List<Question>>> getQuestons(@Path("productId") int productId);
}

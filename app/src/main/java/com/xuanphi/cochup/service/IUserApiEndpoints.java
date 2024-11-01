package com.xuanphi.cochup.service;

import com.xuanphi.cochup.dto.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IUserApiEndpoints {
    @GET("Users")
    Call<User> login(@Query("username") String username, @Query("password") String password);

    @POST("Users/Register")
    Call<User> register(@Body User userResponse);
}

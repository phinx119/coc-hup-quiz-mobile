package com.xuanphi.cochup.service;

import com.xuanphi.cochup.dto.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ICategoryApiEndpoints {

    @GET("Categories")
    Call<List<Category>> getAllCategory();

}

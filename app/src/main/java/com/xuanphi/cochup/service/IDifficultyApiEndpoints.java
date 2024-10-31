package com.xuanphi.cochup.service;

import com.xuanphi.cochup.dto.Category;
import com.xuanphi.cochup.dto.Difficulty;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IDifficultyApiEndpoints {

    @GET("Difficulties")
    Call<List<Difficulty>> getAllDifficulty();

}

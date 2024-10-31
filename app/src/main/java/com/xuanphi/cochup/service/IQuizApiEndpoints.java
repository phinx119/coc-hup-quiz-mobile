package com.xuanphi.cochup.service;

import com.xuanphi.cochup.dto.Quiz;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IQuizApiEndpoints {

    @GET("api.php")
    Call<Quiz> getAllQuiz(
            @Query("amount") int amount,
            @Query("category") int category,
            @Query("difficulty") String difficulty,
            @Query("type") String type
    );

}

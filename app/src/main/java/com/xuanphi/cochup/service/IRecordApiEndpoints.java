package com.xuanphi.cochup.service;

import com.xuanphi.cochup.dto.Record;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IRecordApiEndpoints {

    @GET("Records/GetListByCategoryAndDifficulty")
    Call<List<Record>> getRecordsByCategoryAndDifficulty(
            @Query("categoryName") String categoryName,
            @Query("difficultyName") String difficultyName
    );

    @GET("Records/GetListByUserId")
    Call<List<Record>> getRecordsByUserId(@Query("userId") int userId);

    @GET("Records/GetByUserId")
    Call<Record> getRecordByUserId(
            @Query("userId") int userId,
            @Query("categoryName") String categoryName,
            @Query("difficultyName") String difficultyName
    );

    @POST("Records")
    Call<Record> createRecord(
            @Query("userId") int userId,
            @Query("categoryName") String categoryName,
            @Query("difficultyName") String difficultyName,
            @Query("highScore") int highScore
    );

    @PUT("Records")
    Call<Record> updateRecord(
            @Query("userId") int userId,
            @Query("categoryName") String categoryName,
            @Query("difficultyName") String difficultyName,
            @Query("highScore") int highScore
    );

}

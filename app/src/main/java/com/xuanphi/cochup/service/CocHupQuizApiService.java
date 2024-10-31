package com.xuanphi.cochup.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CocHupQuizApiService {

    public static final String BASE_URL = "http://localhost/api/";

    private final ICategoryApiEndpoints iCategoryApiEndpoints;

    private static CocHupQuizApiService cocHupQuizApiService;

    public static CocHupQuizApiService getInstance() {
        if(cocHupQuizApiService == null) {
            cocHupQuizApiService = new CocHupQuizApiService();
        }
        return cocHupQuizApiService;
    }

    public CocHupQuizApiService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)  // Increase connection timeout
                .readTimeout(30, TimeUnit.SECONDS)     // Increase read timeout
                .writeTimeout(30, TimeUnit.SECONDS)    // Increase write timeout
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        iCategoryApiEndpoints = retrofit.create(ICategoryApiEndpoints.class);
    }

    public static ICategoryApiEndpoints getICategoryApiEndpoints() {
        return getInstance().iCategoryApiEndpoints;
    }

}

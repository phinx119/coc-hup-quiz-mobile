package com.xuanphi.cochup.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CocHupQuizApiService {

    public static final String PHI_BASE_URL = "http://192.168.80.88:5000/api/";
    public static final String PHONG_BASE_URL = "http://192.168.50.100:5000/api/";

    private final ICategoryApiEndpoints iCategoryApiEndpoints;
    private final IDifficultyApiEndpoints iDifficultyApiEndpoints;
    private final IUserApiEndpoints iUserApiEndpoints;

    private static CocHupQuizApiService cocHupQuizApiService;

    public static CocHupQuizApiService getInstance() {
        if (cocHupQuizApiService == null) {
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
                .baseUrl(PHONG_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        iCategoryApiEndpoints = retrofit.create(ICategoryApiEndpoints.class);
        iDifficultyApiEndpoints = retrofit.create(IDifficultyApiEndpoints.class);
        iUserApiEndpoints = retrofit.create(IUserApiEndpoints.class);
    }

    public static ICategoryApiEndpoints getICategoryApiEndpoints() {
        return getInstance().iCategoryApiEndpoints;
    }

    public static IDifficultyApiEndpoints getIDifficultyApiEndpoint() {
        return getInstance().iDifficultyApiEndpoints;
    }

    public static IUserApiEndpoints getUserService() {
        return getInstance().iUserApiEndpoints;
    }
}

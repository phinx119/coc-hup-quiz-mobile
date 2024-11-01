package com.xuanphi.cochup.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CocHupQuizApiService {

    public static final String PHI_BASE_URL = "http://192.168.80.88:5000/api/";
    public static final String PHONG_BASE_URL = "http://192.168.50.100:5000/api/";
    public static final String FPT_BASE_URL = "http://10.33.45.186:5000/api/";

    private final IUserApiEndpoints iUserApiEndpoints;
    private final ICategoryApiEndpoints iCategoryApiEndpoints;
    private final IDifficultyApiEndpoints iDifficultyApiEndpoints;
    private final IRecordApiEndpoints iRecordApiEndpoints;

    private static CocHupQuizApiService cocHupQuizApiService;

    public static CocHupQuizApiService getInstance() {
        if (cocHupQuizApiService == null) {
            cocHupQuizApiService = new CocHupQuizApiService();
        }
        return cocHupQuizApiService;
    }

    public CocHupQuizApiService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PHI_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        iUserApiEndpoints = retrofit.create(IUserApiEndpoints.class);
        iCategoryApiEndpoints = retrofit.create(ICategoryApiEndpoints.class);
        iDifficultyApiEndpoints = retrofit.create(IDifficultyApiEndpoints.class);
        iRecordApiEndpoints = retrofit.create(IRecordApiEndpoints.class);
    }

    public static IUserApiEndpoints getUserService() {
        return getInstance().iUserApiEndpoints;
    }

    public static ICategoryApiEndpoints getICategoryApiEndpoints() {
        return getInstance().iCategoryApiEndpoints;
    }

    public static IDifficultyApiEndpoints getIDifficultyApiEndpoint() {
        return getInstance().iDifficultyApiEndpoints;
    }

    public static IRecordApiEndpoints getIRecordApiEndpoints() {
        return getInstance().iRecordApiEndpoints;
    }
}

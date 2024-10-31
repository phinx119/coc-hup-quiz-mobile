package com.xuanphi.cochup.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenTriviaDBApiService {

    public static final String BASE_URL = "https://opentdb.com/";

    private final IQuizApiEndpoints iQuizApiEndpoints;

    private static OpenTriviaDBApiService openTriviaDBApiService;

    public static OpenTriviaDBApiService getInstance() {
        if(openTriviaDBApiService == null) {
            openTriviaDBApiService = new OpenTriviaDBApiService();
        }
        return openTriviaDBApiService;
    }

    public OpenTriviaDBApiService() {
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) .build();

        iQuizApiEndpoints = retrofit.create(IQuizApiEndpoints.class);
    }

    public static IQuizApiEndpoints getIQuizApiEndpoints() {
        return getInstance().iQuizApiEndpoints;
    }

}

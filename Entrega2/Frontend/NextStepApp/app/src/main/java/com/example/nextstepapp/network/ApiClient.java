package com.example.nextstepapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {

    private static final String BASE_URL =
            "http://10.0.2.2:3000/";

    private static ApiService apiService;

    private ApiClient() {
    }
    public static ApiService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();apiService = retrofit.create(ApiService.class
            );
        }
        return apiService;
    }
}
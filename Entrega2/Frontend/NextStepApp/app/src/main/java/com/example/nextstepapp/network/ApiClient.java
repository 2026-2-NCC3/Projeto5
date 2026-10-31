package com.example.nextstepapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {

    private static final String BASE_URL =
            "https://projeto5-7yqt.onrender.com/";

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
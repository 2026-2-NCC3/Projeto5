package com.example.nextstepapp.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {

    private static final String BASE_URL = "https://projeto5-7yqt.onrender.com/";
    private static volatile ApiService apiService;

    private ApiClient() {
    }

    public static ApiService getApiService() {
        ApiService service = apiService;
        if (service == null) {
            synchronized (ApiClient.class) {
                service = apiService;
                if (service == null) {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    service = retrofit.create(ApiService.class);
                    apiService = service;
                }
            }
        }
        return service;
    }
}

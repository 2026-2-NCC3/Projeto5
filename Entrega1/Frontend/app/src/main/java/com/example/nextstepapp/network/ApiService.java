package com.example.nextstepapp.network;

import com.example.nextstepapp.model.ApiResponse;
import com.example.nextstepapp.model.Curso;
import com.example.nextstepapp.model.LoginData;
import com.example.nextstepapp.model.LoginRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/auth/login")
    Call<ApiResponse<LoginData>> login(
            @Body LoginRequest request
    );

    @GET("api/courses")
    Call<ApiResponse<List<Curso>>> getCursos();

    @GET("api/courses/schedule")
    Call<ApiResponse<List<Curso>>> getAgenda(
            @Header("Authorization") String authorization
    );
}
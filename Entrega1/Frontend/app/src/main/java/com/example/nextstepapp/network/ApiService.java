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
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

// =====================================================
// AUTENTICAÇÃO
// =====================================================

    @POST("api/auth/login")
    Call<ApiResponse<LoginData>> login(
            @Body LoginRequest request
    );

    @POST("api/auth/register")
    Call<ApiResponse<LoginData>> register(
            @Body LoginRequest request
    );


// =====================================================
// CURSOS
// =====================================================

    @GET("api/courses")
    Call<ApiResponse<List<Curso>>> getCursos();


// =====================================================
// AGENDA
// =====================================================

    @GET("api/courses/schedule")
    Call<ApiResponse<List<Curso>>> getAgenda(
            @Header("Authorization") String authorization
    );


// =====================================================
// PERFIL
// =====================================================

    @GET("api/profile")
    Call<ApiResponse<Object>> getProfile(
            @Header("Authorization") String authorization
    );

    @PUT("api/profile")
    Call<ApiResponse<Object>> updateProfile(
            @Header("Authorization") String authorization,
            @Body Object profile
    );


// =====================================================
// CERTIFICADOS
// =====================================================

    @GET("api/certificates")
    Call<ApiResponse<List<Object>>> getCertificates(
            @Header("Authorization") String authorization
    );


// =====================================================
// CONQUISTAS
// =====================================================

    @GET("api/badges")
    Call<ApiResponse<List<Object>>> getBadges(
            @Header("Authorization") String authorization
    );


// =====================================================
// TESTES
// =====================================================

    @GET("api/tests")
    Call<ApiResponse<List<Object>>> getTests();

    @GET("api/tests/results")
    Call<ApiResponse<List<Object>>> getTestResults(
            @Header("Authorization") String authorization
    );

    @POST("api/tests/results")
    Call<ApiResponse<Object>> saveTestResult(
            @Header("Authorization") String authorization,
            @Body Object result
    );


// =====================================================
// NOTÍCIAS
// =====================================================

    @GET("api/news")
    Call<ApiResponse<List<Object>>> getNews();

    @GET("api/news/{id}")
    Call<ApiResponse<Object>> getNewsById(
            @Path("id") String id
    );


// =====================================================
// COMENTÁRIOS
// =====================================================

    @GET("api/news/{id}/comments")
    Call<ApiResponse<List<Object>>> getNewsComments(
            @Path("id") String id
    );

    @POST("api/news/{id}/comments")
    Call<ApiResponse<Object>> addNewsComment(
            @Header("Authorization") String authorization,
            @Path("id") String id,
            @Body Object comment
    );


// =====================================================
// VÍDEOS / PODCASTS
// =====================================================

    @GET("api/videos")
    Call<ApiResponse<List<Object>>> getVideos();

    @GET("api/videos/{id}")
    Call<ApiResponse<Object>> getVideoById(
            @Path("id") String id
    );


// =====================================================
// NOTIFICAÇÕES
// =====================================================

    @GET("api/notifications")
    Call<ApiResponse<List<Object>>> getNotifications(
            @Header("Authorization") String authorization
    );

    @PATCH("api/notifications/{id}/read")
    Call<ApiResponse<Object>> markNotificationRead(
            @Header("Authorization") String authorization,
            @Path("id") String id
    );
}
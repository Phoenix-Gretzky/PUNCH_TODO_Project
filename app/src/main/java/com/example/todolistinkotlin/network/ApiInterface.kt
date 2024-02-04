package com.example.todolistinkotlin.network

import android.content.Context
import com.example.todolistinkotlin.MyApplication
import com.example.todolistinkotlin.model.TodoData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface  {

    //    https://baseurl/


    @POST("analytics/data")
    suspend fun sendAnalyticsData( @Query("previousData") previous: TodoData,@Query("data") data: TodoData): Response<String>



    @POST("analytics/delete/data")
    suspend fun sendDeleteAnalyticsData(@Query("previousData") previous: TodoData): Response<String>

    @POST("/api/v1/users/refresh-token/")
    suspend fun refreshAccessToken(): TokenResponse

}


class RetrofitFactory {
    companion object {


         var context:Context=MyApplication.applicationContext();


        //we have added the Auth interceptor to safeguard our apis now before everyApi this interceptor will validate the Auth Token
        private val client = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .addInterceptor(AuthInterceptor( context))
            .build()
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://baseURL/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val service: ApiInterface by lazy {
            retrofit.create(ApiInterface::class.java)
        }
    }


    // api service will get fed to the auth interceptor for refresh token api
    init {
        AuthInterceptor.apiInterface= service;
    }
}
package com.example.todolistinkotlin.network

import android.content.Context
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor(context: Context) : Interceptor {
    private val sessionManager = SessionManager(context)


    companion object{
         lateinit var apiInterface: ApiInterface;
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = sessionManager.fetchAuthToken()

        if (accessToken != null && sessionManager.isAccessTokenExpired()) {

            // If the access token is present and expired then refresh token request will be made and we will get the refresh token
            // Make the token refresh request
            val refreshedToken = runBlocking {
                val response = apiInterface.refreshAccessToken()
                // Update the refreshed access token and its expiration time in the session
                sessionManager.updateAccessToken(response.accessToken, response.expiresIn)
                response.accessToken
            }

            if (refreshedToken != null) {
                // Create a new request with the refreshed access token
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $refreshedToken")
                    .build()

                // Retry the request with the new access token
                return chain.proceed(newRequest)
            }
        }

        // Add the access token to the request header
        val authorizedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(authorizedRequest)
    }





}
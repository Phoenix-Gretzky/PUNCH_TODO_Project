package com.example.todolistinkotlin.network

import android.content.Context
import android.content.SharedPreferences
import com.example.todolistinkotlin.R

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private var accessTokenExpirationTime: Long? = null

    companion object {
        const val USER_TOKEN = "user_token"
    }

    /**
     * Function to save auth token
     * now we call this function when we login or do some kind of verification process but as this app doesnt have so
     * so we take it we already have saved the USER_TOKEN
     * which we use for secure and authenticated api calls
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }


    // Method to check if the access token has expired
    fun isAccessTokenExpired(): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        return accessTokenExpirationTime != null && currentTimeMillis >= accessTokenExpirationTime!!
    }

    // Method to update the access token and its expiration time in the session
    fun updateAccessToken(token: String, expiresIn: Long) {
        saveAuthToken(token)
        accessTokenExpirationTime =
            System.currentTimeMillis() + expiresIn * 1000 // Convert expiresIn to milliseconds
    }


    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
}
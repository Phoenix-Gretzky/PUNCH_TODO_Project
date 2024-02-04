package com.example.todolistinkotlin.network

import com.google.gson.annotations.SerializedName


/*this class is for storing and recieving the Token from the login and refresh token api */
data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Long
)
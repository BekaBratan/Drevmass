package com.example.drevmass.data.api

import com.example.drevmass.data.model.AuthorizationResponse
import com.example.drevmass.data.model.LoginRequest
import com.example.drevmass.data.model.RegistrationRequest
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body authorization: LoginRequest
    ): AuthorizationResponse

    @POST("signup")
    suspend fun registration(
        @Body registration: RegistrationRequest
    ): AuthorizationResponse

    @FormUrlEncoded
    @POST("forgot_password")
    suspend fun forgotPassword(
        @Field("email") email: String
    ): AuthorizationResponse
}
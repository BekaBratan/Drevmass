package com.example.drevmass.data.api

import com.example.drevmass.data.model.LoginRequest
import com.example.drevmass.data.model.LoginResponse
import retrofit2.http.Body
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
    ): LoginResponse

}
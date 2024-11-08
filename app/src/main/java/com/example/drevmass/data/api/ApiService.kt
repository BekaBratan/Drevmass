package com.example.drevmass.data.api

import com.example.drevmass.data.model.AuthorizationResponse
import com.example.drevmass.data.model.LoginRequest
import com.example.drevmass.data.model.MessageResponse
import com.example.drevmass.data.model.RegistrationRequest
import com.example.drevmass.data.model.ResetPasswordRequest
import com.example.drevmass.data.model.bonus.BonusResponse
import com.example.drevmass.data.model.userInfo.UserInfoRequest
import com.example.drevmass.data.model.userInfo.UserInfoResponse
import com.example.drevmass.data.model.userPromocode.UserPromocodeResponse
import kz.mobydev.drevmass.model.promocode.PromocodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

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

    @GET("bonus")
    suspend fun getBonus(@Header("Authorization") token: String): BonusResponse

    @GET("user/information")
    suspend fun getUserInfo(@Header("Authorization") token: String): UserInfoResponse

    @POST("user/information")
    suspend fun postUserInfo(
        @Header("Authorization") token: String,
        @Body request: UserInfoRequest
    ): UserInfoResponse

    @GET("user/promocode")
    suspend fun getUserPromocode(@Header("Authorization") token: String): UserPromocodeResponse

    @POST("activate")
    @FormUrlEncoded
    suspend fun activatePromocode(
        @Header("Authorization") token: String,
        @Field("promocode") promocode: String
    ): Response<PromocodeResponse>

    @POST("reset_password")
    suspend fun resetPassword(
        @Header("Authorization") token: String,
        @Body request: ResetPasswordRequest
    ): MessageResponse
}
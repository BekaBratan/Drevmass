package com.example.drevmass.data.model

data class AuthorizationResponse (
    val access_token: String?,
    val refresh_token: String?,
    val code: String?,
    val message: String?,
    val metadata: String?
)
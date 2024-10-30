package com.example.drevmass.data.model

data class LoginRequest(
    val deviceToken: String,
    val email: String,
    val password: String
)
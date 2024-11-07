package com.example.drevmass.data.model

data class RegistrationRequest(
    val deviceToken: String,
    val email: String,
    val name: String,
    val password: String,
    val phone_number: String
)
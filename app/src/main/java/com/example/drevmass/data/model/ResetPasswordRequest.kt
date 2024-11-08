package com.example.drevmass.data.model


import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("current_password")
    val currentPassword: String, // string
    @SerializedName("new_password")
    val newPassword: String // string
)
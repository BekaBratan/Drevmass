package com.example.drevmass.data.model.userPromocode


import com.google.gson.annotations.SerializedName

data class UserPromocodeResponse(
    @SerializedName("bonus")
    val bonus: Int,
    @SerializedName("promocode")
    val promocode: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("all_attempt")
    val allAttempt: Int,
    @SerializedName("used")
    val used: Int
)
package com.example.drevmass.data.model.userPromocode


import com.google.gson.annotations.SerializedName

data class UserPromocodeResponse(

    @SerializedName("bonus")
    val bonus: Int, // 1000
    @SerializedName("promocode")
    val promocode: String, // 321IHHY2
    @SerializedName("description")
    val description: String, // for registration
    @SerializedName("all_attempt")
    val allAttempt: Int, // 2
    @SerializedName("used")
    val used: Int // 0
)
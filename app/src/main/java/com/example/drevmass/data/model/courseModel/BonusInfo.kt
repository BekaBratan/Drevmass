package com.example.drevmass.data.model.courseModel


import com.google.gson.annotations.SerializedName

data class BonusInfo(
    @SerializedName("promo_type")
    val promoType: String, // course
    @SerializedName("price")
    val price: Int, // 500
    @SerializedName("description")
    val description: String, // for course ending
    @SerializedName("deadline")
    val deadline: String
)
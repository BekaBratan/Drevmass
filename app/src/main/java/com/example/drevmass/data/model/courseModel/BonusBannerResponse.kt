package com.example.drevmass.data.model.courseModel


import com.google.gson.annotations.SerializedName

data class BonusBannerResponse(
    @SerializedName("promo_type")
    val promoType: String, // course
    @SerializedName("price")
    val price: Int, // 600
    @SerializedName("description")
    val description: String, // Бонус за завершение курса
    @SerializedName("deadline")
    val deadline: String // 90
)
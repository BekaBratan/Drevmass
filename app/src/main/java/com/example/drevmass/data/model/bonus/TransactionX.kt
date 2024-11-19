package com.example.drevmass.data.model.bonus


import com.google.gson.annotations.SerializedName

data class TransactionX(
    @SerializedName("userid")
    val userid: Int, // 21
    @SerializedName("description")
    val description: String, // Списание при покупке
    @SerializedName("promo_price")
    val promoPrice: Int, // 50
    @SerializedName("transaction_type")
    val transactionType: String, // -
    @SerializedName("transaction_date")
    val transactionDate: String, // 2024-01-05 17:39:35
    @SerializedName("promo_type")
    val promoType: String,
    @SerializedName("bonus_id")
    val bonusId: Int // 0
)
package com.example.drevmass.data.model.bonus


import com.google.gson.annotations.SerializedName

data class BonusResponse(
    @SerializedName("bonus")
    val bonus: Int, // 4750
    @SerializedName("user_id")
    val userId: Int, // 21
    @SerializedName("Burning")
    val burning: List<Burning>,
    @SerializedName("Transactions")
    val transactions: List<TransactionX>
)
package com.example.drevmass.data.model.bonus


import com.google.gson.annotations.SerializedName

data class Burning(
    @SerializedName("activated_date")
    val activatedDate: String,
    @SerializedName("bonus")
    val bonus: Int,
    @SerializedName("burning_date")
    val burningDate: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("userid")
    val userid: Int
)
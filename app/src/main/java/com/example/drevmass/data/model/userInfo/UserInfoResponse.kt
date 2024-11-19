package com.example.drevmass.data.model.userInfo


import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("id")
    val id: Int, // 21
    @SerializedName("email")
    val email: String, // 123@gmail.com
    @SerializedName("name")
    val name: String, // string
    @SerializedName("gender")
    val gender: Int, // 0
    @SerializedName("height")
    val height: Int, // 178
    @SerializedName("weight")
    val weight: Int, // 190
    @SerializedName("birth")
    val birth: String, // string
    @SerializedName("activity")
    val activity: Int, // 1
    @SerializedName("phone_number")
    val phoneNumber: String // string
)
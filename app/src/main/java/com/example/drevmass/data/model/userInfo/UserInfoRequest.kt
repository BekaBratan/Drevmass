package com.example.drevmass.data.model.userInfo


import com.google.gson.annotations.SerializedName

data class UserInfoRequest(
    @SerializedName("id")
    val id: Int, // 1
    @SerializedName("email")
    val email: String, // 123@gmail.com
    @SerializedName("name")
    val name: String, // Arman
    @SerializedName("gender")
    val gender: Int, // 0
    @SerializedName("height")
    val height: Int, // 187
    @SerializedName("weight")
    val weight: Int, // 72
    @SerializedName("birth")
    val birth: String, // 16-02-1998
    @SerializedName("activity")
    val activity: Int, // 2
    @SerializedName("phone_number")
    val phoneNumber: String // 8-778-984-23-24
)
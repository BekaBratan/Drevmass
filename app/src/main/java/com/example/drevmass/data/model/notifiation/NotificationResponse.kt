package com.example.drevmass.data.model.notifiation


import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("user_id")
    val userId: Int, // 20
    @SerializedName("course_id")
    val courseId: Int, // 18
    @SerializedName("mon")
    val mon: Boolean, // false
    @SerializedName("tue")
    val tue: Boolean, // false
    @SerializedName("wed")
    val wed: Boolean, // false
    @SerializedName("thu")
    val thu: Boolean, // false
    @SerializedName("fri")
    val fri: Boolean, // false
    @SerializedName("sat")
    val sat: Boolean, // false
    @SerializedName("sun")
    val sun: Boolean, // false
    @SerializedName("time")
    val time: String,
    @SerializedName("notificationIsSelected")
    val notificationIsSelected: Boolean // false
)
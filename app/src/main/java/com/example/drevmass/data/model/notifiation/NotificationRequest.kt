package com.example.drevmass.data.model.notifiation


import com.google.gson.annotations.SerializedName

data class NotificationRequest(
    @SerializedName("course_id")
    val courseId: Int, // 0
    @SerializedName("fri")
    val fri: Boolean, // true
    @SerializedName("mon")
    val mon: Boolean, // true
    @SerializedName("notificationIsSelected")
    val notificationIsSelected: Boolean, // true
    @SerializedName("sat")
    val sat: Boolean, // true
    @SerializedName("sun")
    val sun: Boolean, // true
    @SerializedName("thu")
    val thu: Boolean, // true
    @SerializedName("time")
    val time: String, // string
    @SerializedName("tue")
    val tue: Boolean, // true
    @SerializedName("user_id")
    val userId: Int, // 0
    @SerializedName("wed")
    val wed: Boolean // true
)
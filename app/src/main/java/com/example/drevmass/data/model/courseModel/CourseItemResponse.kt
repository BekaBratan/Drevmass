package com.example.drevmass.data.model.courseModel


import com.example.drevmass.data.model.courseModel.BonusInfo
import com.google.gson.annotations.SerializedName

data class CourseItemResponse(
    @SerializedName("id")
    val id: Int, // 14
    @SerializedName("name")
    val name: String, // New Course
    @SerializedName("duration")
    val duration: Int, // 10
    @SerializedName("lesson_cnt")
    val lessonCnt: Int, // 1
    @SerializedName("image_src")
    val imageSrc: String,
    @SerializedName("completed")
    val completed: Boolean, // false
    @SerializedName("bonus_info")
    val bonusInfo: BonusInfo?
)
package com.example.drevmass.data.model.courseModel


import com.google.gson.annotations.SerializedName

data class CourseInfoResponse(
    @SerializedName("all_lessons")
    val allLessons: Int,
    @SerializedName("completed_lessons")
    val completedLessons: Int,
    @SerializedName("course")
    val course: CourseResponse?,
    @SerializedName("bonus_info")
    val bonusInfo: BonusInfo?
)
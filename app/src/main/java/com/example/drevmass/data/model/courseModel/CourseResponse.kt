package com.example.drevmass.data.model.courseModel


import com.example.drevmass.data.model.courseModel.BonusInfo
import com.example.drevmass.data.model.courseModel.LessonResponse
import com.google.gson.annotations.SerializedName

data class CourseResponse(
    @SerializedName("id")
    val id: Int, // 2
    @SerializedName("name")
    val name: String, // Внедрение занятий на Древмасс за 21 день
    @SerializedName("description")
    val description: String,
    @SerializedName("duration")
    val duration: Int, // 107
    @SerializedName("lesson_cnt")
    val lessonCnt: Int, // 6
    @SerializedName("is_started")
    val isStarted: Boolean, // false
    @SerializedName("image_src")
    val imageSrc: String,
    @SerializedName("lessons")
    val lessons: List<LessonResponse>,
    @SerializedName("completed")
    val completed: Boolean, // false
    @SerializedName("bonus_info")
    val bonusInfo: BonusInfo?
)
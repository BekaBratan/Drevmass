package com.example.drevmass.data.model.courseModel


import com.example.drevmass.data.model.courseModel.LessonResponse
import com.google.gson.annotations.SerializedName

data class FavoriteCourseListResponseItem(
    @SerializedName("course_id")
    val courseId: Int, // 11
    @SerializedName("course_name")
    val courseName: String, // name2
    @SerializedName("lessons")
    val lessons: List<LessonResponse> // []
)
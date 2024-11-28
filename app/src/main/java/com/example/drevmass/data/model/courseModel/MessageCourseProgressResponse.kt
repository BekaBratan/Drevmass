package com.example.drevmass.data.model.courseModel


import com.google.gson.annotations.SerializedName

data class MessageCourseProgressResponse(
    @SerializedName("course_progress")
    val message: String // string
)
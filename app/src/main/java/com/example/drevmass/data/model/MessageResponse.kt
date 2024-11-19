package com.example.drevmass.data.model


import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message")
    val message: String // lesson added to favorites
)
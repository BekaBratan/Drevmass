package com.example.drevmass.data.model.courseModel

import androidx.core.app.NotificationCompat.MessagingStyle.Message
import com.google.gson.annotations.SerializedName

data class MessageCompletedModel (
    @SerializedName("completed")
    val completedMessage: Boolean
)
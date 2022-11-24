package com.example.android_sns_project.data

import androidx.core.app.NotificationCompat

data class PushDTO(
        var to: String? = null,
        var notification: Notification = Notification()
){
        data class Notification(
                var body: String ?= null,
                var title: String? = null
        )
}
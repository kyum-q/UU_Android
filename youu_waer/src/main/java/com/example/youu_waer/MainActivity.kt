package com.example.youu_waer

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract.EXTRA_EVENT_ID
import com.example.youu_waer.databinding.ActivityMainBinding
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat.WearableExtender

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
//        val notificationId = 1
//        // The channel ID of the notification.
//        val id = "my_channel_01"
//        // Build intent for notification content
//        val viewPendingIntent = Intent(this, Activity::class.java).let { viewIntent ->
//            val eventId = 1
//            viewIntent.putExtra(EXTRA_EVENT_ID, eventId)
//            PendingIntent.getActivity(this, 0, viewIntent, 0)
//        }
//
//
//        // Notification channel ID is ignored for Android 7.1.1
//        // (API level 25) and lower.
//        val notificationBuilder = NotificationCompat.Builder(this, id)
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContentTitle("watch")
//            .setContentText("test test")
//            .setContentIntent(viewPendingIntent)
//
//        NotificationManagerCompat.from(this).apply {
//            notify(notificationId, notificationBuilder.build())
//        }

    }
}
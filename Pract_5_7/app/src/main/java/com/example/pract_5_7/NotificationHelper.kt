package com.example.pract_5_7

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import androidx.core.app.NotificationCompat

abstract class NotificationHelper {

    private val CHANNEL_ID = "Test"
    private val CHANNEL_NAME = "Name"


    fun createNotificationChannel(context: Context) {
        val notificationManager: NotificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    abstract fun createMessage(time: Int)
}
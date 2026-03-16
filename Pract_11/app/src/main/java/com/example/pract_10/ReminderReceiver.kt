package com.example.pract_10

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

@SuppressLint("MissingPermission")
class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        showNotification(context)

        val prefs = context.getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)
        val isEnabled = prefs.getBoolean("is_enabled", false)

        if (isEnabled) {
            AlarmScheduler.schedule(context)
        }
    }

    private fun showNotification(context: Context) {
        val notification = NotificationCompat.Builder(context, MyApplication.NotificationChannelConst.CHANNEL_ID)
            .setContentTitle("Напоминание о таблетке")
            .setContentText("Время принять таблетку!")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(MyApplication.NotificationChannelConst.importance)
            .setAutoCancel(true)
            .build()

        androidx.core.app.NotificationManagerCompat.from(context)
            .notify(1, notification)
    }
}
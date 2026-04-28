package com.example.pract_10

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return

        val prefs = context.getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)
        val isEnabled = prefs.getBoolean("is_enabled", false)

        if (isEnabled) {
            AlarmScheduler.schedule(context)
        }
    }
}
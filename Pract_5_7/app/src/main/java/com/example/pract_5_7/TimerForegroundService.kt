package com.example.pract_5_7

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private class ForegroundNotificationHelper() {

    private val CHANNEL_ID = "Test"
    private val CHANNEL_NAME = "Name"


    fun createNotificationChannel(context: Context) {
        val notificationManager: NotificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    fun createMessage(context: Context, time: Int) = NotificationCompat.Builder(context,CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Timer")
        .setContentText("Прошло $time секунд")
        .setOngoing(true)
        .build()
}

class TimerForegroundService : Service() {

    var time = 0
    private val SERVICE_ID = 1000
    private val serviceScope = CoroutineScope(Dispatchers.Main)
    private var timerJob: Job? = null
    private val binder = LocalBinder()
    private val notificationHelper = ForegroundNotificationHelper()


    inner class LocalBinder : Binder() {
        fun getService(): TimerForegroundService = this@TimerForegroundService
    }

    fun timerStart() {
        time = 0
        timerJob = serviceScope.launch {
            while (true) {
                delay(1000L)
                time += 1
                CountState.time.value = time
                updateNotification()
            }
        }
    }

    fun timerStop(){
        timerJob?.cancel()
        updateNotification()
    }

    override fun onBind(intent: Intent): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        notificationHelper.createNotificationChannel(this)
    }

    private fun updateNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(SERVICE_ID, notificationHelper.createMessage(this, time))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationHelper.createNotificationChannel(this)
        startForeground(SERVICE_ID,notificationHelper.createMessage(this, time))
        timerStart()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        timerStop()
        serviceScope.cancel()
        super.onDestroy()
    }

}

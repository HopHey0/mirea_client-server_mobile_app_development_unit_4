package com.example.pract_5_7

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BackgroundNotificationHelper() {

    private val CHANNEL_ID = "Test"
    private val CHANNEL_NAME = "Name"


    fun createNotificationChannel(context: Context) {
        val notificationManager: NotificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    fun createMessage(context: Context) = NotificationCompat.Builder(context,CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Таймер завершён")
        .setContentText("Время вышло!")
        .build()
}

class TimerBackgroundService: Service() {
    var time = CountState.time.value

    val notifHelper = BackgroundNotificationHelper()

    fun countDown(){
        CoroutineScope(Dispatchers.IO).launch{
            while (time > 0) {
                delay(1000L)
                time -= 1
            }
            sendFinishNotification()
        }
    }

    fun sendFinishNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            1001,
            notifHelper.createMessage(this)
        )

        //Toast.makeText(this, "Время вышло!", Toast.LENGTH_LONG).show()
        stopSelf()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show()
        countDown()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show()
    }

}
package com.example.pract_9

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


class MyApplication : Application() {
    val appModule = module {
        viewModel { WeatherScreenViewModel(androidApplication()) }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startKoin {
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NotificationChannelConst.CHANNEL_ID,
            NotificationChannelConst.name,
            NotificationChannelConst.importance
        ).apply { description = NotificationChannelConst.descriptionText }
        val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    object NotificationChannelConst {
        val CHANNEL_ID = "TEST"
        val name = "sample"
        val descriptionText = "Sample Notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
    }
}

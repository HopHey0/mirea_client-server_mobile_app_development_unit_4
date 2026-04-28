package com.example.pract_9

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.random.nextLong

class LoadWeatherWorker(
    context: Context,
    parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return createForegroundInfo("Загружаем погоду...")
    }

    override suspend fun doWork(): Result {
        val cityName = inputData.getString("city_name") ?: return Result.failure()
        val temp = Random.nextInt(-20..20)

        return try {
            delay(Random.nextLong(5000L..15000L))
            Result.success(
                workDataOf("city_name" to cityName, "city_temp" to temp)
            )
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun createForegroundInfo(message: String): ForegroundInfo {
        val notification = NotificationCompat.Builder(applicationContext, MyApplication.NotificationChannelConst.CHANNEL_ID)
            .setContentTitle("Прогноз погоды")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .build()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                1,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(1, notification)
        }
    }
}

@SuppressLint("MissingPermission")
class FinalWorker(
    context: Context,
    parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return createForegroundInfo("Формируем отчёт...")
    }

    override suspend fun doWork(): Result {
        delay(2000L)
        val temps = inputData.getIntArray("city_temp")

        val avgTemp = temps?.average()?.toInt() ?: 0

        setForeground(createForegroundInfo("Все данные получены формируем отчёт..."))
        delay(2000L)
        Log.d("Final_notif", "Pre final notif")
        createFinalNotification("Отчёт готов! Средняя температура $avgTemp°C")
        Log.d("Final_notif", "Post final notif")
        return Result.success()
    }

    private fun createForegroundInfo(message: String): ForegroundInfo {
        val notification = NotificationCompat.Builder(
            applicationContext,
            MyApplication.NotificationChannelConst.CHANNEL_ID
        )
            .setContentTitle("Прогноз погоды")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .build()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                1,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(1, notification)
        }
    }
    private fun createFinalNotification(message: String) {
        val notification = NotificationCompat.Builder(applicationContext, MyApplication.NotificationChannelConst.CHANNEL_ID)
            .setContentTitle("Прогноз погоды")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setOngoing(false)
            .build()

        NotificationManagerCompat.from(applicationContext)
            .notify(2, notification)
    }
}
package com.example.pract_8_9

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import kotlin.random.Random

class PhotoLoadWorker1(
    context: Context,
    parameters: WorkerParameters
): CoroutineWorker(context, parameters) {
    override suspend fun doWork(): Result {
        return try {
            delay(5000L)
            if (Random.nextBoolean()) throw Exception("Worker1 failed")
            Log.d("Worker1", "Worker1 success")
            Result.success()
        } catch (e: Exception){
            Log.d("Worker1", "Worker1 failure")
            Result.failure()
        }
    }
}

class PhotoLoadWorker2(
    context: Context,
    parameters: WorkerParameters
): CoroutineWorker(context, parameters) {
    override suspend fun doWork(): Result {
        return try {
            delay(4000L)
            if (Random.nextBoolean()) throw Exception("Worker2 failed")
            Log.d("Worker2", "Worker2 success")
            Result.success()
        } catch (e: Exception){
            Log.d("Worker2", "Worker2 failure")
            Result.failure()
        }
    }
}

class PhotoLoadWorker3(
    context: Context,
    parameters: WorkerParameters
): CoroutineWorker(context, parameters) {
    override suspend fun doWork(): Result {
        return try {
            delay(6000L)
            if (Random.nextBoolean()) throw Exception("Worker3 failed")
            Log.d("Worker3", "Worker3 success")
            Result.success()
        } catch (e: Exception){
            Log.d("Worker3", "Worker3 failure")
            Result.failure()
        }
    }
}
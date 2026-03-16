package com.example.pract_9

import android.R
import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ArrayCreatingInputMerger
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.setInputMerger
import androidx.work.workDataOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
class WeatherScreenViewModel(
    application: Application
): AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(WeatherScreenUiState(cityList = cityList))
    val uiState = _uiState.asStateFlow()

    private val workManager = WorkManager.getInstance(application)

    private var gettingWeatherJob: Job? = null

    fun getWeather() {
        _uiState.update {uiState ->
            uiState.copy(
                cityList = uiState.cityList.map { city ->
                    city.copy(isLoading = true, error = false)
                },
                isLoading = true,
                isLoaded = false)
        }

        val workList = _uiState.value.cityList.map { city ->
            OneTimeWorkRequestBuilder<LoadWeatherWorker>()
                .setInputData(workDataOf("city_name" to city.name, "city_temp" to city.temp))
                .addTag("weather_worker")
                .build()
        }

        val finalWork = OneTimeWorkRequestBuilder<FinalWorker>()
            .addTag("final_weather_worker")
            .setInputMerger(ArrayCreatingInputMerger::class)
            .build()

        workManager
            .beginUniqueWork("Get_weather_work", ExistingWorkPolicy.REPLACE ,workList)
            .then(finalWork)
            .enqueue()

        showNotification("Загружаем погоду для ${_uiState.value.cityList.size} городов...")

        gettingWeatherJob = viewModelScope.launch {
            val doneCities = mutableListOf<String>()

            workManager.getWorkInfosForUniqueWorkFlow("Get_weather_work")
                .collect { workInfoList ->
                    if (!_uiState.value.isLoading) return@collect

                    val weatherWorkList = workInfoList.filter { it.tags.contains("weather_worker") }
                    if (weatherWorkList.isEmpty()) return@collect

                    val isAnyRunning = weatherWorkList.any {
                        it.state == WorkInfo.State.RUNNING || it.state == WorkInfo.State.ENQUEUED
                    }

                    val countOfRunning = weatherWorkList.count {
                        it.state == WorkInfo.State.RUNNING || it.state == WorkInfo.State.ENQUEUED
                    }

                    _uiState.update {
                        it.copy(
                            loadStatus = "Загрузка... (${countOfRunning} в процессе)"
                        )
                    }

                    Log.d("WeatherVM", "workInfoList size: ${workInfoList.size}")

                    weatherWorkList.forEach { workInfo ->
                        Log.d("WeatherVM", "state: ${workInfo.state}, progress: ${workInfo.progress}")

                        val cityName = workInfo.outputData.getString("city_name") ?: return@forEach

                        if (workInfo.state == WorkInfo.State.SUCCEEDED && !doneCities.contains(cityName)) {
                            doneCities.add(cityName)
                        }

                        _uiState.update { uiState ->
                            uiState.copy(
                                cityList = uiState.cityList.map { city ->
                                    val workInfoLocal = weatherWorkList.find { city.name == it.outputData.getString("city_name")}
                                    when (workInfoLocal?.state){
                                        WorkInfo.State.ENQUEUED, WorkInfo.State.BLOCKED -> city.copy(state = "Ожидание", isLoading = false)
                                        WorkInfo.State.RUNNING -> city.copy(state = "Загружается...", isLoading = true)
                                        WorkInfo.State.SUCCEEDED -> city.copy(temp = "${workInfoLocal.outputData.getInt("city_temp", defaultValue = 0)}°C", state = "Загружено", isLoading = false)
                                        WorkInfo.State.CANCELLED, WorkInfo.State.FAILED -> city.copy(state = "Прервано", isLoading = false, error = true)
                                        else -> city
                                    }
                                }
                            )
                        }
                    }

                    val inProgress = _uiState.value.cityList
                        .map { it.name }
                        .filter { !doneCities.contains(it) }

                    when {
                        doneCities.isEmpty() -> Unit

                        inProgress.isNotEmpty() -> {
                            val doneText = doneCities.joinToString(" и ")
                            val progressText = inProgress.joinToString(", ")
                            showNotification("Готово: $doneText \n $progressText в процессе...")
                        }

                        !isAnyRunning -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isLoaded = true,
                                    loadStatus = "Все данные получены!"
                                )
                            }
                        }
                    }
                }
        }
    }

    fun cancelGettingWeather() {
        workManager.cancelUniqueWork("Get_weather_work")

        gettingWeatherJob?.cancel()

        _uiState.update {uiState ->
            uiState.copy(
                cityList = uiState.cityList.map { city ->
                    city.copy(temp = "", isLoading = false, state = "Ожидание")
                },
                isLoading = false,
                isLoaded = false,
                loadStatus = "Ожидание")
        }

        NotificationManagerCompat.from(getApplication())
            .cancel(1)
    }

    private fun showNotification(message: String) {
        val notification = NotificationCompat.Builder(getApplication(), MyApplication.NotificationChannelConst.CHANNEL_ID)
            .setContentTitle("Прогноз погоды")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setOngoing(true)
            .build()

        NotificationManagerCompat.from(getApplication())
            .notify(1, notification)
    }

    override fun onCleared() {
        super.onCleared()
        cancelGettingWeather()
    }
}
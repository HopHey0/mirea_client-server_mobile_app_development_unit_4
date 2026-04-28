package com.example.pract_8_9

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class PhotoLoaderViewModel(application: Application) : AndroidViewModel(application) {

    private val workManager = WorkManager.getInstance(application)

    private val _uiState = MutableStateFlow(PhotoLoaderUiState())
    val uiState = _uiState.asStateFlow()

    private var progressJob: Job? = null

    fun startLoading() {
        _uiState.update { it.copy(isLoading = true) }

        val worker1 = OneTimeWorkRequestBuilder<PhotoLoadWorker1>().build()
        val worker2 = OneTimeWorkRequestBuilder<PhotoLoadWorker2>().build()
        val worker3 = OneTimeWorkRequestBuilder<PhotoLoadWorker3>().build()

        workManager.beginWith(worker1)
            .then(worker2)
            .then(worker3)
            .enqueue()

        simulateProgress(worker1.id, worker2.id, worker3.id)
    }

    private fun simulateProgress(id1: UUID, id2: UUID, id3: UUID) {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {

            val states = setOf(
                WorkInfo.State.SUCCEEDED,
                WorkInfo.State.FAILED,
                WorkInfo.State.CANCELLED
            )

            fun WorkInfo?.isFailed() = this?.state in setOf(WorkInfo.State.FAILED, WorkInfo.State.CANCELLED)

            _uiState.update { it.copy(headerStatusText = "Запущена загрузка...", photoStatusText = "Это может занять несколько секунд...", error = false) }

            if (workManager.getWorkInfoByIdFlow(id1).first { it?.state in states }.isFailed()) {
                return@launch _uiState.update { it.copy(headerStatusText = "Ошибка! Попробуйте позже", photoStatusText = "", isLoading = false, error = true) }
            }
            _uiState.update { it.copy(headerStatusText = "Обработка и загрузка фото...") }

            if (workManager.getWorkInfoByIdFlow(id2).first { it?.state in states }.isFailed()) {
                return@launch _uiState.update { it.copy(headerStatusText = "Ошибка! Попробуйте позже", photoStatusText = "", isLoading = false, error = true) }
            }
            _uiState.update { it.copy(headerStatusText = "Грузим в облако...") }

            if (workManager.getWorkInfoByIdFlow(id3).first { it?.state in states }.isFailed()) {
                return@launch _uiState.update { it.copy(headerStatusText = "Ошибка! Попробуйте позже", photoStatusText = "", isLoading = false, error = true) }
            }

            _uiState.update { it.copy(headerStatusText = "Загрузка завершена!", photoStatusText = "Ссылка на загруженное фото: https://cloud.mirea.ru/uploaded_dyg4ndhf3jd34.jpg", isLoading = false) }
        }
    }


    override fun onCleared() {
        super.onCleared()
        progressJob?.cancel()
    }
}
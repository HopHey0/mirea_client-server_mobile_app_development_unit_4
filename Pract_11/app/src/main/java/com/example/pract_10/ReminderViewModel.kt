package com.example.pract_10

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.content.edit


class ReminderViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(ReminderUiState(isEnabled = prefs.getBoolean("is_enabled", false)))
    val uiState = _uiState.asStateFlow()

    init {
        if (_uiState.value.isEnabled) {
            updateNextReminderText()
        }
    }

    fun enableReminder() {
        AlarmScheduler.schedule(getApplication())
        prefs.edit { putBoolean("is_enabled", true) }
        _uiState.update { it.copy(isEnabled = true) }
        updateNextReminderText()
    }

    fun disableReminder() {
        AlarmScheduler.cancel(getApplication())
        prefs.edit { putBoolean("is_enabled", false) }
        _uiState.update { it.copy(isEnabled = false, nextReminderText = "") }
    }

    private fun updateNextReminderText() {
        val nextTime = AlarmScheduler.getNextReminderTime()
        val isToday = isSameDay(nextTime, System.currentTimeMillis())
        val dayWord = if (isToday) "сегодня" else "завтра"
        _uiState.update {
            it.copy(nextReminderText = "Следующее напоминание: $dayWord в 20:00")
        }
    }

    private fun isSameDay(time1: Long, time2: Long): Boolean {
        val fmt = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        return fmt.format(Date(time1)) == fmt.format(Date(time2))
    }
}
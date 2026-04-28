package com.example.pract_13

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class ExchangeRateViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeRateUiState())
    val uiState = _uiState.asStateFlow()

    private var rateJob: Job? = null


    init {
        rateJob = viewModelScope.launch {
            while (true) {
                delay(5000L)
                updateRate()
            }
        }
    }

    fun refreshRate() {
        updateRate()
    }

    private fun updateRate() {
        val newRate = 90.5 + Random.nextDouble(-2.0, 2.0)
        _uiState.update { it.copy(rate = newRate, isGreater = (newRate >= it.rate)) }
    }

    override fun onCleared() {
        super.onCleared()
        rateJob?.cancel()
    }
}
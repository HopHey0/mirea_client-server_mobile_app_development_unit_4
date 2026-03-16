package com.example.pract_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class AnimalFactViewModel(): ViewModel() {

    private val _uiState = MutableStateFlow(AnimalFactScreenUiState())
    val uiState = _uiState.asStateFlow()

    private var loadFactJob: Job? = null

    private fun getRandomFact(): Flow<String> = flow {
        delay(3000L)
        emit(animalFactsExampleList[Random.nextInt(animalFactsExampleList.size)])
    }

    fun loadRandomFact(){
        loadFactJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getRandomFact().collect { fact ->
                _uiState.update { uiState ->
                    uiState.copy(isLoading = false, animalFactText = fact)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        loadFactJob = null
    }
}
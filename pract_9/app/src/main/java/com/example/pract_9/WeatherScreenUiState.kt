package com.example.pract_9

data class WeatherScreenUiState(
    val cityList: List<CityWeather> = emptyList(),
    val loadStatus: String = "Готов начать!",
    val isLoading: Boolean = false,
    val isLoaded: Boolean = false
)

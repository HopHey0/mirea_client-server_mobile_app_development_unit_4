package com.example.pract_10

data class GeoScreenUiState(
    val coarseGranted: Boolean = false,
    val fineGranted: Boolean = false,
    val headerText: String = "Получить геолокацию",
    val bodyText: String = "",
    val isLoading: Boolean = false
)

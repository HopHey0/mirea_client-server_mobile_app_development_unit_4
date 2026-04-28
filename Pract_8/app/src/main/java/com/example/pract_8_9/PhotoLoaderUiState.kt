package com.example.pract_8_9

data class PhotoLoaderUiState(
    val isLoading: Boolean = false,
    val headerStatusText: String = "Фото готово к загрузке",
    val photoStatusText: String = "",
    val error: Boolean = false
)

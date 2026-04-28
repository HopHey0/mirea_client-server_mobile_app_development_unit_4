package com.example.pract_5_7

import androidx.core.text.isDigitsOnly
import kotlinx.coroutines.flow.MutableStateFlow

object CountState {
    val time = MutableStateFlow(0)

    fun changeTime(newValue: Int){
        time.value = newValue
    }
}
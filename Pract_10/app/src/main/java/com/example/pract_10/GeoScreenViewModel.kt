package com.example.pract_10

import android.annotation.SuppressLint
import android.app.Application
import android.location.Geocoder
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class GeoScreenViewModel(
    application: Application):
    AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(GeoScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    fun onPermissionResult(isCoarseGranted: Boolean, isFineGranted: Boolean){
        _uiState.update { it.copy(coarseGranted = isCoarseGranted, fineGranted = isFineGranted) }
    }

    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        _uiState.update { it.copy(isLoading = true) }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    resolveAddress(location.latitude, location.longitude)
                } else {
                    _uiState.update { it.copy(headerText = "Не удалось получить локацию", isLoading = false) }
                }
            }
            .addOnFailureListener { exception ->
                _uiState.update { it.copy(headerText = exception.toString(), isLoading = false) }
            }
    }

    private fun resolveAddress(lat: Double, lng: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(3000L)
                //throw IOException("Something wrong happened while geocoding")
                val geocoder = Geocoder(getApplication(), Locale.getDefault())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(lat, lng, 1) { addresses ->
                        _uiState.update { it.copy(
                            headerText = "${addresses.firstOrNull()?.getAddressLine(0)}",
                            bodyText = "$lat $lng",
                            isLoading = false
                        )  }
                    }
                } else {
                    val addresses = geocoder.getFromLocation(lat, lng, 1)
                    _uiState.update { it.copy(

                        headerText = "${addresses?.firstOrNull()?.getAddressLine(0)}",
                        bodyText = "$lat $lng",
                        isLoading = false
                    )  }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(headerText = "Ошибка геокодирования: ${e.message}", isLoading = false) }
            }
        }
    }

}
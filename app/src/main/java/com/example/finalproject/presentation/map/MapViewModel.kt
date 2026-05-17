package com.example.finalproject.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.domain.model.Incident
import com.example.finalproject.domain.usecase.IncidentUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel(private val incidentUseCases: IncidentUseCases) : ViewModel() {
    private val _incidents = MutableStateFlow<List<Incident>>(emptyList())
    val incidents: StateFlow<List<Incident>> = _incidents

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadIncidents(lat: Double, lng: Double, radius: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            runCatching {
                incidentUseCases.getIncidentsNearby(lat, lng, radius)
            }.onSuccess { nearbyIncidents ->
                _incidents.value = nearbyIncidents
            }.onFailure { throwable ->
                _errorMessage.value = throwable.message ?: "Unable to load incidents."
            }

            _isLoading.value = false
        }
    }
}

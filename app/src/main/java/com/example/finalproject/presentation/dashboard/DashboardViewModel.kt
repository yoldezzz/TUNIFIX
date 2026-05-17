package com.example.finalproject.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.domain.model.Incident
import com.example.finalproject.domain.usecase.IncidentUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(private val incidentUseCases: IncidentUseCases) : ViewModel() {
    private val _incidents = MutableStateFlow<List<Incident>>(emptyList())
    val incidents: StateFlow<List<Incident>> = _incidents

    fun loadAllIncidents() {
        viewModelScope.launch {
            // For authority: load all or filtered incidents
            _incidents.value = incidentUseCases.getIncidentsNearby(0.0, 0.0, 10000.0)
        }
    }

    fun verifyIncident(incidentId: String) {
        viewModelScope.launch {
            incidentUseCases.verifyIncident(incidentId)
            loadAllIncidents()
        }
    }

    fun resolveIncident(incidentId: String) {
        viewModelScope.launch {
            incidentUseCases.resolveIncident(incidentId)
            loadAllIncidents()
        }
    }
}

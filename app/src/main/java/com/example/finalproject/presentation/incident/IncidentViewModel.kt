package com.example.finalproject.presentation.incident

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.domain.model.Incident
import com.example.finalproject.domain.usecase.IncidentUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IncidentViewModel(private val incidentUseCases: IncidentUseCases) : ViewModel() {
    private val _incidents = MutableStateFlow<List<Incident>>(emptyList())
    val incidents: StateFlow<List<Incident>> = _incidents

    fun reportIncident(incident: Incident) {
        viewModelScope.launch {
            incidentUseCases.reportIncident(incident)
            loadNearbyIncidents(incident.location?.first ?: 0.0, incident.location?.second ?: 0.0, 5.0)
        }
    }

    fun loadNearbyIncidents(lat: Double, lng: Double, radius: Double) {
        viewModelScope.launch {
            _incidents.value = incidentUseCases.getIncidentsNearby(lat, lng, radius)
        }
    }

    fun upvoteIncident(incidentId: String) {
        viewModelScope.launch {
            incidentUseCases.upvoteIncident(incidentId)
        }
    }

    fun commentOnIncident(incidentId: String, comment: String) {
        viewModelScope.launch {
            incidentUseCases.commentOnIncident(incidentId, comment)
        }
    }
}

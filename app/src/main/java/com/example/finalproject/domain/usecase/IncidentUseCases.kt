package com.example.finalproject.domain.usecase

import com.example.finalproject.domain.repository.IncidentRepository

class IncidentUseCases(private val incidentRepository: IncidentRepository) {
    suspend fun reportIncident(incident: com.example.finalproject.domain.model.Incident) = incidentRepository.reportIncident(incident)
    suspend fun getIncidentsNearby(lat: Double, lng: Double, radius: Double) = incidentRepository.getIncidentsNearby(lat, lng, radius)
    suspend fun upvoteIncident(incidentId: String) = incidentRepository.upvoteIncident(incidentId)
    suspend fun commentOnIncident(incidentId: String, comment: String) = incidentRepository.commentOnIncident(incidentId, comment)
    suspend fun getIncidentById(incidentId: String) = incidentRepository.getIncidentById(incidentId)
    suspend fun verifyIncident(incidentId: String) = incidentRepository.verifyIncident(incidentId)
    suspend fun resolveIncident(incidentId: String) = incidentRepository.resolveIncident(incidentId)
}
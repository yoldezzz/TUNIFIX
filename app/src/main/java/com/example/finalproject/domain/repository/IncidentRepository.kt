package com.example.finalproject.domain.repository

import com.example.finalproject.domain.model.Incident

interface IncidentRepository {
    suspend fun reportIncident(incident: Incident): Boolean
    suspend fun getIncidentsNearby(lat: Double, lng: Double, radius: Double): List<Incident>
    suspend fun upvoteIncident(incidentId: String): Boolean
    suspend fun commentOnIncident(incidentId: String, comment: String): Boolean
    suspend fun getIncidentById(incidentId: String): Incident?
    suspend fun verifyIncident(incidentId: String): Boolean
    suspend fun resolveIncident(incidentId: String): Boolean
}
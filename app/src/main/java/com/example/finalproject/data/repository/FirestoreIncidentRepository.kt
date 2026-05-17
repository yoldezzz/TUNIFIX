package com.example.finalproject.data.repository

import com.example.finalproject.domain.model.Incident
import com.example.finalproject.domain.repository.IncidentRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirestoreIncidentRepository : IncidentRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val incidentsRef = firestore.collection("incidents")

    override suspend fun verifyIncident(incidentId: String): Boolean {
        incidentsRef.document(incidentId).update("status", "verified").await()
        return true
    }

    override suspend fun resolveIncident(incidentId: String): Boolean {
        incidentsRef.document(incidentId).update("status", "resolved").await()
        return true
    }

    override suspend fun reportIncident(incident: Incident): Boolean {
        val doc = incidentsRef.document()
        val data = hashMapOf(
            "id" to doc.id,
            "type" to incident.type,
            "severity" to incident.severity,
            "description" to incident.description,
            "mediaUrls" to incident.mediaUrls,
            "location" to incident.location?.let { listOf(it.first, it.second) },
            "timestamp" to incident.timestamp,
            "status" to incident.status,
            "reporterId" to incident.reporterId,
            "upvotes" to incident.upvotes,
            "comments" to incident.comments
        )
        doc.set(data).await()
        return true
    }

    override suspend fun getIncidentsNearby(lat: Double, lng: Double, radius: Double): List<Incident> {
        // Simple fetch: get all incidents (for demo; use geoqueries for production)
        val snapshot = incidentsRef.get().await()
        return snapshot.documents.mapNotNull { doc ->
            val loc = doc.get("location") as? List<*>
            Incident(
                id = doc.getString("id") ?: "",
                type = doc.getString("type") ?: "",
                severity = doc.getString("severity") ?: "",
                description = doc.getString("description") ?: "",
                mediaUrls = (doc.get("mediaUrls") as? List<*>)?.map { it.toString() } ?: emptyList(),
                location = if (loc != null && loc.size == 2) Pair((loc[0] as Number).toDouble(), (loc[1] as Number).toDouble()) else null,
                timestamp = doc.getLong("timestamp") ?: 0L,
                status = doc.getString("status") ?: "pending",
                reporterId = doc.getString("reporterId") ?: "",
                upvotes = (doc.getLong("upvotes") ?: 0L).toInt(),
                comments = (doc.get("comments") as? List<*>)?.map { it.toString() } ?: emptyList()
            )
        }
    }

    override suspend fun upvoteIncident(incidentId: String): Boolean {
        val doc = incidentsRef.document(incidentId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(doc)
            val upvotes = (snapshot.getLong("upvotes") ?: 0L) + 1
            transaction.update(doc, "upvotes", upvotes)
        }.await()
        return true
    }

    override suspend fun commentOnIncident(incidentId: String, comment: String): Boolean {
        val doc = incidentsRef.document(incidentId)
        doc.update("comments", com.google.firebase.firestore.FieldValue.arrayUnion(comment)).await()
        return true
    }

    override suspend fun getIncidentById(incidentId: String): Incident? {
        val doc = incidentsRef.document(incidentId).get().await()
        if (!doc.exists()) return null
        val loc = doc.get("location") as? List<*>
        return Incident(
            id = doc.getString("id") ?: "",
            type = doc.getString("type") ?: "",
            severity = doc.getString("severity") ?: "",
            description = doc.getString("description") ?: "",
            mediaUrls = (doc.get("mediaUrls") as? List<*>)?.map { it.toString() } ?: emptyList(),
            location = if (loc != null && loc.size == 2) Pair((loc[0] as Number).toDouble(), (loc[1] as Number).toDouble()) else null,
            timestamp = doc.getLong("timestamp") ?: 0L,
            status = doc.getString("status") ?: "pending",
            reporterId = doc.getString("reporterId") ?: "",
            upvotes = (doc.getLong("upvotes") ?: 0L).toInt(),
            comments = (doc.get("comments") as? List<*>)?.map { it.toString() } ?: emptyList()
        )
    }

    suspend fun uploadMedia(bytes: ByteArray, fileName: String): String {
        val ref = storage.reference.child("incident_media/$fileName")
        ref.putBytes(bytes).await()
        return ref.downloadUrl.await().toString()
    }
}

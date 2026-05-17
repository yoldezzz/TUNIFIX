package com.example.finalproject.domain.model

data class Notification(
    val id: String = "",
    val userId: String = "",
    val type: String = "",
    val message: String = "",
    val incidentId: String? = null,
    val timestamp: Long = 0L
)
package com.example.finalproject.domain.model

data class Incident(
    val id: String = "",
    val type: String = "",
    val severity: String = "",
    val description: String = "",
    val mediaUrls: List<String> = emptyList(),
    val location: Pair<Double, Double>? = null, // lat, lng
    val timestamp: Long = 0L,
    val status: String = "pending", // pending, verified, resolved
    val reporterId: String = "",
    val upvotes: Int = 0,
    val comments: List<String> = emptyList()
)
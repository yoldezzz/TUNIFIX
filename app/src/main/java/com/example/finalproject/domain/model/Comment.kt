package com.example.finalproject.domain.model

data class Comment(
    val id: String = "",
    val incidentId: String = "",
    val userId: String = "",
    val text: String = "",
    val timestamp: Long = 0L
)
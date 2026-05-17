package com.example.finalproject.domain.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "citizen", // citizen, authority, admin
    val profilePic: String? = null
)
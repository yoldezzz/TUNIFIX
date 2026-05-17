package com.example.finalproject.domain.repository

import com.example.finalproject.domain.model.User

interface UserRepository {
    suspend fun getCurrentUser(): User?
    suspend fun signIn(email: String, password: String): User?
    suspend fun signUp(email: String, password: String, name: String): User?
    suspend fun signOut()
    suspend fun signInWithGoogle(idToken: String): User?
    suspend fun signInAsGuest(): User
}
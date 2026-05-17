package com.example.finalproject.domain.usecase

import com.example.finalproject.domain.repository.UserRepository

class AuthUseCases(private val userRepository: UserRepository) {
    suspend fun signIn(email: String, password: String) = userRepository.signIn(email, password)
    suspend fun signUp(email: String, password: String, name: String) = userRepository.signUp(email, password, name)
    suspend fun signOut() = userRepository.signOut()
    suspend fun signInWithGoogle(idToken: String) = userRepository.signInWithGoogle(idToken)
    suspend fun signInAsGuest() = userRepository.signInAsGuest()
    suspend fun getCurrentUser() = userRepository.getCurrentUser()
}
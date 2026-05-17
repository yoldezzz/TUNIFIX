package com.example.finalproject.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.domain.model.User
import com.example.finalproject.domain.usecase.AuthUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authUseCases: AuthUseCases) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user
    val error = MutableStateFlow<String?>(null)

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _user.value = authUseCases.signIn(email, password)
            } catch (e: Exception) {
                error.value = e.message
            }
        }
    }

    fun signUp(email: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                _user.value = authUseCases.signUp(email, password, name)
            } catch (e: Exception) {
                error.value = e.message
            }
        }
    }

    fun signInAsGuest() {
        viewModelScope.launch {
            try {
                _user.value = authUseCases.signInAsGuest()
            } catch (e: Exception) {
                error.value = e.message
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authUseCases.signOut()
            _user.value = null
        }
    }
}

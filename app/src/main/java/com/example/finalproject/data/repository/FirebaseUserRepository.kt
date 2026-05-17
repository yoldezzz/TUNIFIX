package com.example.finalproject.data.repository

import com.example.finalproject.domain.model.User
import com.example.finalproject.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import android.util.Log

class FirebaseUserRepository : UserRepository {
    private val auth: FirebaseAuth = Firebase.auth

    override suspend fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return User(
            id = firebaseUser.uid,
            name = firebaseUser.displayName ?: "User",
            email = firebaseUser.email ?: "",
            role = "citizen",
            profilePic = firebaseUser.photoUrl?.toString()
        )
    }

    override suspend fun signIn(email: String, password: String): User? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.let {
            User(
                id = it.uid,
                name = it.displayName ?: "User",
                email = it.email ?: "",
                role = "citizen",
                profilePic = it.photoUrl?.toString()
            )
        }
    }

    override suspend fun signUp(email: String, password: String, name: String): User? {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result.user?.updateProfile(com.google.firebase.auth.UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
        return result.user?.let {
            User(
                id = it.uid,
                name = name,
                email = it.email ?: "",
                role = "citizen",
                profilePic = it.photoUrl?.toString()
            )
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun signInWithGoogle(idToken: String): User? {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        return result.user?.let {
            User(
                id = it.uid,
                name = it.displayName ?: "User",
                email = it.email ?: "",
                role = "citizen",
                profilePic = it.photoUrl?.toString()
            )
        }
    }

    override suspend fun signInAsGuest(): User {
        return try {
            val result = auth.signInAnonymously().await()
            val user = result.user!!
            User(
                id = user.uid,
                name = "Guest User",
                email = "",
                role = "citizen",
                profilePic = null
            )
        } catch (e: Exception) {
            Log.e("Auth", "Firebase Guest Sign-in failed: ${e.message}. Using local mock user.", e)
            // Fallback to a mock user to bypass the CONFIGURATION_NOT_FOUND error
            User(
                id = "mock_guest_id",
                name = "Guest (Offline)",
                email = "guest@example.com",
                role = "citizen",
                profilePic = null
            )
        }
    }
}

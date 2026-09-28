package ru.matveev.gigachat.domain.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow
import ru.matveev.gigachat.domain.state.AuthState

interface AuthRepository {
    val currentUser: FirebaseUser?
    val authState: StateFlow<AuthState>
    suspend fun signIn(email: String, password: String): AuthState
    suspend fun signUp(email: String, password: String): AuthState
    suspend fun signInWithGoogle(idToken: String): AuthState
    fun signOut(): AuthState
}
package ru.matveev.gigachat.core.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.matveev.gigachat.core.domain.state.AuthState
import ru.matveev.gigachat.core.error.AppResult
import ru.matveev.gigachat.core.model.User

interface AuthRepository {
    val currentUser: User?
    val authState: StateFlow<AuthState>
    suspend fun signIn(email: String, password: String): AppResult<Unit>
    suspend fun signUp(email: String, password: String): AppResult<Unit>
    suspend fun signInWithGoogle(idToken: String): AppResult<Unit>
    fun signOut(): AppResult<Unit>
}

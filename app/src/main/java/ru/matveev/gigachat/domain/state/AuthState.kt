package ru.matveev.gigachat.domain.state

import ru.matveev.gigachat.domain.DomainError

sealed interface AuthState {
    data object Idle : AuthState
    data object Loading : AuthState
    data object Authenticated : AuthState
    data object Unauthenticated : AuthState
    data class Error(val error: DomainError) : AuthState
}
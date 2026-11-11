package ru.matveev.gigachat.core.domain.state

sealed interface AuthState {
    data object Authenticated : AuthState
    data object Unauthenticated : AuthState
}

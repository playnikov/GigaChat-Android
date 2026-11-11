package ru.matveev.gigachat.feature.auth.signin

import ru.matveev.gigachat.core.error.DomainError

sealed interface SignInEvent {
    data class EmailUpdated(val newEmail: String) : SignInEvent
    data class PasswordUpdated(val newPassword: String) : SignInEvent
    data object VisiblePasswordUpdated : SignInEvent
    data object SignIn : SignInEvent

    data class GoogleTokenReceived(val idToken: String) : SignInEvent
    data class GoogleTokenFailed(val error: DomainError) : SignInEvent
}

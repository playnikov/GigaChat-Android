package ru.matveev.gigachat.ui.auth.register

sealed interface SignUpEvent {
    data class EmailUpdated(val newEmail: String) : SignUpEvent
    data class PasswordUpdated(val newPassword: String) : SignUpEvent
    data class ConfirmPasswordUpdated(val newPassword: String) : SignUpEvent
    data object VisiblePasswordUpdated : SignUpEvent
    data object VisibleConfirmPasswordUpdated : SignUpEvent

    data object SignUp : SignUpEvent
}
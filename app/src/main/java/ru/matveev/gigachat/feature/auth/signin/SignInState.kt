package ru.matveev.gigachat.feature.auth.signin

import ru.matveev.gigachat.core.domain.validation.AuthValidator

data class SignInState(
    val email: String = "",
    val password: String = "",
    val isVisiblePassword: Boolean = false,
    val isLoading: Boolean = false
) {
    val isSubmitEnabled: Boolean
        get() = AuthValidator.isValidEmail(email)
                && AuthValidator.isValidPassword(password)
                && !isLoading
}

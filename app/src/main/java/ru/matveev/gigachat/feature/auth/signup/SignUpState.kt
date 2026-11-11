package ru.matveev.gigachat.feature.auth.signup

import ru.matveev.gigachat.core.domain.validation.AuthValidator

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isVisiblePassword: Boolean = false,
    val isVisibleConfirmPassword: Boolean = false,
    val isLoading: Boolean = false
) {
    val isSubmitEnabled: Boolean
        get() = AuthValidator.isValidEmail(email)
                && AuthValidator.isValidPassword(password)
                && password == confirmPassword
                && !isLoading
}

package ru.matveev.gigachat.ui.auth.login

data class SignInState(
    val email: String = "",
    val password: String = "",
    val isVisiblePassword: Boolean = false,
    val isLoading: Boolean = false
) {
    val isSubmitEnabled: Boolean
        get() = email.isNotBlank()
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length >= 8
                && !isLoading
}
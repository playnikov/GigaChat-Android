package ru.matveev.gigachat.ui.auth.register

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isVisiblePassword: Boolean = false,
    val isVisibleConfirmPassword: Boolean = false,
    val isLoading: Boolean = false
) {
    val isSubmitEnabled: Boolean
        get() = email.isNotBlank()
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length >= 8
                && !isLoading
                && password == confirmPassword
}
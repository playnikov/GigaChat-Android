package ru.matveev.gigachat.feature.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.matveev.gigachat.core.domain.usecase.SignUpUseCase
import ru.matveev.gigachat.core.error.AppResult
import ru.matveev.gigachat.core.ui.Effect
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.ConfirmPasswordUpdated -> onConfirmPasswordUpdated(event.newPassword)
            is SignUpEvent.EmailUpdated -> onEmailUpdated(event.newEmail)
            is SignUpEvent.PasswordUpdated -> onPasswordUpdated(event.newPassword)
            SignUpEvent.SignUp -> signUp()
            SignUpEvent.VisiblePasswordUpdated -> onVisiblePasswordUpdated()
            SignUpEvent.VisibleConfirmPasswordUpdated -> onVisibleConfirmPasswordUpdated()
        }
    }

    private fun signUp() {
        val current = _state.value
        if (current.isLoading || !current.isSubmitEnabled) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = signUpUseCase(current.email, current.password)) {
                is AppResult.Success -> _effect.send(Effect.Success)
                is AppResult.Failure -> _effect.send(Effect.ShowError(result.error.message))
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun onEmailUpdated(newEmail: String) {
        _state.update { it.copy(email = newEmail) }
    }

    private fun onPasswordUpdated(newPassword: String) {
        _state.update { it.copy(password = newPassword) }
    }

    private fun onConfirmPasswordUpdated(newPassword: String) {
        _state.update { it.copy(confirmPassword = newPassword) }
    }

    private fun onVisiblePasswordUpdated() {
        _state.update { it.copy(isVisiblePassword = !it.isVisiblePassword) }
    }

    private fun onVisibleConfirmPasswordUpdated() {
        _state.update { it.copy(isVisibleConfirmPassword = !it.isVisibleConfirmPassword) }
    }
}

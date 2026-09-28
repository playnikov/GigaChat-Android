package ru.matveev.gigachat.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.matveev.gigachat.data.toDomainError
import ru.matveev.gigachat.domain.state.AuthState
import ru.matveev.gigachat.domain.use_case.SignInUseCase
import ru.matveev.gigachat.domain.use_case.SignInWithGoogleUseCase
import ru.matveev.gigachat.ui.Effect
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>(extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.EmailUpdated -> onEmailUpdated(event.newEmail)
            is SignInEvent.PasswordUpdated -> onPasswordUpdated(event.newPassword)
            SignInEvent.VisiblePasswordUpdated -> onVisiblePasswordUpdated()
            SignInEvent.SignIn -> signIn()
            is SignInEvent.GoogleTokenReceived -> signInWithGoogle(event.idToken)
            is SignInEvent.GoogleTokenFailed -> onGoogleTokenFailed(event.error)
        }
    }

    private fun signIn() {
        val current = _state.value
        if (current.isLoading || !current.isSubmitEnabled) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = signInUseCase(current.email, current.password)) {
                AuthState.Authenticated -> _effect.emit(Effect.Success)
                is AuthState.Error -> _effect.emit(Effect.ShowError(result.error.message))
                else -> Unit
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = signInWithGoogleUseCase(idToken)) {
                AuthState.Authenticated -> _effect.emit(Effect.Success)
                is AuthState.Error -> _effect.emit(Effect.ShowError(result.error.message))
                else -> Unit
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun onGoogleTokenFailed(error: Throwable) {
        viewModelScope.launch {
            _effect.emit(Effect.ShowError(error.toGoogleDomainError().message))
        }
    }

    private fun onEmailUpdated(newEmail: String) {
        _state.update { it.copy(email = newEmail) }
    }

    private fun onPasswordUpdated(newPassword: String) {
        _state.update { it.copy(password = newPassword) }
    }

    private fun onVisiblePasswordUpdated() {
        _state.update { it.copy(isVisiblePassword = !it.isVisiblePassword) }
    }
}
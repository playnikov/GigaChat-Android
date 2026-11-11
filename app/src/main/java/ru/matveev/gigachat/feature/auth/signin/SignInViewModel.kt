package ru.matveev.gigachat.feature.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.matveev.gigachat.core.domain.usecase.GetCurrentUserUseCase
import ru.matveev.gigachat.core.domain.usecase.SignInUseCase
import ru.matveev.gigachat.core.domain.usecase.SignInWithGoogleUseCase
import ru.matveev.gigachat.core.error.AppResult
import ru.matveev.gigachat.core.error.DomainError
import ru.matveev.gigachat.core.ui.Effect
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        if (getCurrentUserUseCase() != null) {
            _effect.trySend(Effect.Success)
        }
    }

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
                is AppResult.Success -> _effect.send(Effect.Success)
                is AppResult.Failure -> _effect.send(Effect.ShowError(result.error.message))
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = signInWithGoogleUseCase(idToken)) {
                is AppResult.Success -> _effect.send(Effect.Success)
                is AppResult.Failure -> _effect.send(Effect.ShowError(result.error.message))
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun onGoogleTokenFailed(error: DomainError) {
        _effect.trySend(Effect.ShowError(error.message))
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

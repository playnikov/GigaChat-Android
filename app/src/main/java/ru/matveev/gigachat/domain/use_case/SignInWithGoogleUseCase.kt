package ru.matveev.gigachat.domain.use_case

import ru.matveev.gigachat.domain.DomainError
import ru.matveev.gigachat.domain.repository.AuthRepository
import ru.matveev.gigachat.domain.state.AuthState
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): AuthState {
        if (idToken.isBlank()) {
            return AuthState.Error(DomainError.InvalidGoogleToken)
        }
        return authRepository.signInWithGoogle(idToken)
    }
}
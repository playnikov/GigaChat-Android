package ru.matveev.gigachat.core.domain.usecase

import ru.matveev.gigachat.core.domain.repository.AuthRepository
import ru.matveev.gigachat.core.error.AppResult
import ru.matveev.gigachat.core.error.DomainError
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): AppResult<Unit> {
        if (idToken.isBlank()) {
            return AppResult.Failure(DomainError.InvalidGoogleToken)
        }
        return authRepository.signInWithGoogle(idToken)
    }
}

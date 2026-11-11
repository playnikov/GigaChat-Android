package ru.matveev.gigachat.core.domain.usecase

import ru.matveev.gigachat.core.domain.repository.AuthRepository
import ru.matveev.gigachat.core.error.AppResult
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AppResult<Unit> {
        return authRepository.signUp(email.trim(), password)
    }
}

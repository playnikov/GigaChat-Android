package ru.matveev.gigachat.core.domain.usecase

import ru.matveev.gigachat.core.domain.repository.AuthRepository
import ru.matveev.gigachat.core.error.AppResult
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): AppResult<Unit> {
        return authRepository.signOut()
    }
}

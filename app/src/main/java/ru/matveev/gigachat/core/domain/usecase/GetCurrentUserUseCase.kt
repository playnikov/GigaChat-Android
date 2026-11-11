package ru.matveev.gigachat.core.domain.usecase

import ru.matveev.gigachat.core.domain.repository.AuthRepository
import ru.matveev.gigachat.core.model.User
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): User? {
        return authRepository.currentUser
    }
}

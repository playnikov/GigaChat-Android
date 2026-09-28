package ru.matveev.gigachat.domain.use_case

import ru.matveev.gigachat.domain.repository.AuthRepository
import ru.matveev.gigachat.domain.state.AuthState
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthState {
        return authRepository.signUp(email, password)
    }
}
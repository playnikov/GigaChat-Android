package ru.matveev.gigachat.domain.use_case

import com.google.firebase.auth.FirebaseUser
import ru.matveev.gigachat.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): FirebaseUser? {
        return authRepository.currentUser
    }
}
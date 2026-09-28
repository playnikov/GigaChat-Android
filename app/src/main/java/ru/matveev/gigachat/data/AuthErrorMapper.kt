package ru.matveev.gigachat.data

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import ru.matveev.gigachat.domain.DomainError
import java.io.IOException

internal fun Throwable.toDomainError(): DomainError = when (this) {
    is FirebaseAuthInvalidCredentialsException -> DomainError.InvalidCredentials
    is FirebaseAuthInvalidUserException -> DomainError.UserNotFound
    is FirebaseAuthUserCollisionException -> DomainError.UserAlreadyExists
    is FirebaseNetworkException, is IOException -> DomainError.Network
    else -> DomainError.Unknown(message ?: "Неизвестная ошибка")
}
package ru.matveev.gigachat.core.data.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import ru.matveev.gigachat.core.error.DomainError
import java.io.IOException

internal fun Throwable.toDomainError(): DomainError = when (this) {
    is FirebaseAuthInvalidCredentialsException -> DomainError.InvalidCredentials
    is FirebaseAuthInvalidUserException -> DomainError.UserNotFound
    is FirebaseAuthUserCollisionException -> DomainError.UserAlreadyExists
    is FirebaseAuthWeakPasswordException -> DomainError.WeakPassword
    is FirebaseNetworkException, is IOException -> DomainError.Network
    else -> DomainError.Unknown(message ?: "Неизвестная ошибка")
}

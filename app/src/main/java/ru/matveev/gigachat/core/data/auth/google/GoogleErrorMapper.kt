package ru.matveev.gigachat.core.data.auth.google

import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import ru.matveev.gigachat.core.error.DomainError

internal fun Throwable.toGoogleDomainError(): DomainError = when (this) {
    is NoCredentialException -> DomainError.NoGoogleAccount
    is GetCredentialCancellationException -> DomainError.GoogleSignInCancelled
    is GoogleIdTokenParsingException -> DomainError.InvalidGoogleToken
    is GetCredentialException -> DomainError.Unknown(message ?: "Ошибка Google Sign-In")
    else -> DomainError.Unknown(message ?: "Неизвестная ошибка")
}

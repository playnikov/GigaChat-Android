package ru.matveev.gigachat.domain

sealed class DomainError(val message: String) {
    data object Network : DomainError("Нет подключения к интернету")
    data object InvalidCredentials : DomainError("Неверный e-mail или пароль")
    data object UserNotFound : DomainError("Пользователь не найден")
    data object WeakPassword : DomainError("Слишком слабый пароль")
    data object UserAlreadyExists : DomainError("Пользователь уже существует")
    data object InvalidGoogleToken : DomainError("Недействительный Google токен")
    data object NoGoogleAccount : DomainError("На устройстве нет Google-аккаунтов")
    data object GoogleSignInCancelled : DomainError("Вход через Google отменён")
    data class Unknown(val details: String) : DomainError(details)
}
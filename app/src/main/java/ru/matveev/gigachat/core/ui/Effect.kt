package ru.matveev.gigachat.core.ui

sealed interface Effect {
    data class ShowError(val message: String): Effect
    data class ShowInfo(val message: String): Effect
    data object Success: Effect
}
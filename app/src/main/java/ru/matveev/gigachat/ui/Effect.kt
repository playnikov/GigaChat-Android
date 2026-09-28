package ru.matveev.gigachat.ui

sealed interface Effect {
    data class ShowError(val message: String): Effect
    data class ShowInfo(val message: String): Effect
    data object Success: Effect
}
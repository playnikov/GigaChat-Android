package ru.matveev.gigachat.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {
    @Serializable
    data object Auth : Route {
        @Serializable data object SignIn : Route
        @Serializable data object SignUp : Route
    }

    @Serializable
    data object Main : Route {
        @Serializable data object ChatList : Route
        @Serializable data class Chat(val chatId: String) : Route
        @Serializable data object Profile : Route
    }
}
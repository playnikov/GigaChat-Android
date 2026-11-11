package ru.matveev.gigachat.feature.chatlist

import ru.matveev.gigachat.core.model.Chat

data class ChatListState(
    val chats: List<Chat> = emptyList(),
    val query: String = "",
    val isSearchVisible: Boolean = false,
    val isLoading: Boolean = true
) {
    val visibleChats: List<Chat>
        get() = if (query.isBlank()) {
            chats
        } else {
            chats.filter { it.title.contains(query, ignoreCase = true) }
        }
}

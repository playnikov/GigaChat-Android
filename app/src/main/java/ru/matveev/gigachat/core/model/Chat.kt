package ru.matveev.gigachat.core.model

import java.time.Instant

data class Chat(
    val id: String,
    val title: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val lastMessage: String? = null,
    val lastMessageAt: Instant? = null
)

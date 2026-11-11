package ru.matveev.gigachat.core.model

import java.time.Instant

data class Message(
    val id: String,
    val chatId: String,
    val text: String,
    val author: Author,
    val createdAt: Instant,
    val status: Status = Status.Sent
) {
    enum class Author { User, Assistant }
    enum class Status { Sending, Sent, Failed }
}

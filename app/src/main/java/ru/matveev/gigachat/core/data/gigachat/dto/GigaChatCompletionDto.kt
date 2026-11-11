package ru.matveev.gigachat.core.data.gigachat.dto

import kotlinx.serialization.Serializable
import ru.matveev.gigachat.core.model.Message

@Serializable
data class GigaChatCompletionRequest(
    val model: String,
    val messages: List<GigaChatMessageDto>,
    val stream: Boolean
)

@Serializable
data class GigaChatMessageDto(
    val role: String,
    val content: String
)

@Serializable
data class GigaChatCompletionChunk(
    val choices: List<GigaChatChoiceDto> = emptyList()
)

@Serializable
data class GigaChatChoiceDto(
    val delta: GigaChatDeltaDto? = null
)

@Serializable
data class GigaChatDeltaDto(
    val content: String? = null
)

fun Message.toDto(): GigaChatMessageDto = GigaChatMessageDto(
    role = when (author) {
        Message.Author.User -> "user"
        Message.Author.Assistant -> "assistant"
    },
    content = text
)

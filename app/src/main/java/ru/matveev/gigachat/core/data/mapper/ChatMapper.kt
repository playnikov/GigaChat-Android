package ru.matveev.gigachat.core.data.mapper

import ru.matveev.gigachat.core.data.local.entity.ChatEntity
import ru.matveev.gigachat.core.data.local.entity.MessageEntity
import ru.matveev.gigachat.core.data.local.model.ChatWithLastMessage
import ru.matveev.gigachat.core.model.Chat
import ru.matveev.gigachat.core.model.Message
import java.time.Instant

fun ChatEntity.toDomain(): Chat = Chat(
    id = id,
    title = title,
    createdAt = Instant.ofEpochMilli(createdAt),
    updatedAt = Instant.ofEpochMilli(updatedAt),
)

fun ChatWithLastMessage.toDomain(): Chat = chat.toDomain().copy(
    lastMessage = lastMessageText,
    lastMessageAt = lastMessageAt?.let(Instant::ofEpochMilli)
)

fun Chat.toEntity(): ChatEntity = ChatEntity(
    id = id,
    title = title,
    createdAt = createdAt.toEpochMilli(),
    updatedAt = updatedAt.toEpochMilli(),
)

fun MessageEntity.toDomain(): Message = Message(
    id = id,
    chatId = chatId,
    text = text,
    author = Message.Author.valueOf(author),
    createdAt = Instant.ofEpochMilli(createdAt),
    status = Message.Status.valueOf(status)
)

fun Message.toEntity(): MessageEntity = MessageEntity(
    id = id,
    chatId = chatId,
    text = text,
    author = author.name,
    createdAt = createdAt.toEpochMilli(),
    status = status.name
)

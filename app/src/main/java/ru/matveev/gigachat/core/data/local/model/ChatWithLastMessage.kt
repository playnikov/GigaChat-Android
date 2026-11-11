package ru.matveev.gigachat.core.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import ru.matveev.gigachat.core.data.local.entity.ChatEntity

data class ChatWithLastMessage(
    @Embedded val chat: ChatEntity,
    @ColumnInfo(name = "lastMessageText") val lastMessageText: String?,
    @ColumnInfo(name = "lastMessageAt") val lastMessageAt: Long?
)

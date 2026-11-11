package ru.matveev.gigachat.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.matveev.gigachat.core.model.Chat
import ru.matveev.gigachat.core.model.Message

interface ChatRepository {
    fun observeChats(): Flow<List<Chat>>
    fun observeChat(chatId: String): Flow<Chat?>
    fun observeMessages(chatId: String): Flow<List<Message>>

    suspend fun createChat(title: String): Chat
    suspend fun getChat(chatId: String): Chat?
    suspend fun renameChat(chatId: String, title: String)
    suspend fun deleteChat(chatId: String)

    suspend fun getMessages(chatId: String): List<Message>

    suspend fun addMessage(message: Message)
    suspend fun updateMessage(id: String, text: String, status: Message.Status)
    suspend fun updateMessageStatus(id: String, status: Message.Status)
}

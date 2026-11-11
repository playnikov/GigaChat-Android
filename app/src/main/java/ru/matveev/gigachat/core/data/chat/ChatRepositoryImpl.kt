package ru.matveev.gigachat.core.data.chat

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.matveev.gigachat.core.data.local.dao.ChatDao
import ru.matveev.gigachat.core.data.local.dao.MessageDao
import ru.matveev.gigachat.core.data.mapper.toDomain
import ru.matveev.gigachat.core.data.mapper.toEntity
import ru.matveev.gigachat.core.domain.repository.ChatRepository
import ru.matveev.gigachat.core.model.Chat
import ru.matveev.gigachat.core.model.Message
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao
) : ChatRepository {
    override fun observeChats(): Flow<List<Chat>> =
        chatDao.observeAllWithLastMessage()
            .map { list -> list.map { it.toDomain() } }
            .flowOn(Dispatchers.Default)

    override fun observeChat(chatId: String): Flow<Chat?> =
        chatDao.observeById(chatId)
            .map { entity -> entity?.toDomain() }
            .flowOn(Dispatchers.Default)

    override fun observeMessages(chatId: String): Flow<List<Message>> =
        messageDao.observeByChat(chatId)
            .map { list -> list.map { it.toDomain() } }
            .flowOn(Dispatchers.Default)

    override suspend fun createChat(title: String): Chat {
        val now = Instant.now()
        val chat = Chat(
            id = UUID.randomUUID().toString(),
            title = title,
            createdAt = now,
            updatedAt = now
        )
        chatDao.upsert(chat.toEntity())
        return chat
    }

    override suspend fun getChat(chatId: String): Chat? =
        chatDao.getById(chatId)?.toDomain()

    override suspend fun renameChat(chatId: String, title: String) =
        chatDao.rename(chatId, title)

    override suspend fun deleteChat(chatId: String) =
        chatDao.delete(chatId)

    override suspend fun getMessages(chatId: String): List<Message> =
        messageDao.getByChat(chatId).map { it.toDomain() }

    override suspend fun addMessage(message: Message) {
        messageDao.upsert(message.toEntity())
        chatDao.touch(message.chatId, System.currentTimeMillis())
    }

    override suspend fun updateMessage(
        id: String,
        text: String,
        status: Message.Status
    ) {
        messageDao.update(id, text, status.name)
    }

    override suspend fun updateMessageStatus(
        id: String,
        status: Message.Status
    ) {
        messageDao.updateStatus(id, status.name)
    }
}

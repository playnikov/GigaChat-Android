package ru.matveev.gigachat.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import ru.matveev.gigachat.core.domain.repository.ChatRepository
import ru.matveev.gigachat.core.domain.repository.GigaChatRepository
import ru.matveev.gigachat.core.domain.util.ChatTitleGenerator
import ru.matveev.gigachat.core.model.Message
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val gigaChatRepository: GigaChatRepository
) {
    operator fun invoke(chatId: String, text: String, model: String): Flow<String> = flow {
        val userMessage = Message(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            text = text.trim(),
            author = Message.Author.User,
            createdAt = Instant.now(),
            status = Message.Status.Sent
        )
        chatRepository.addMessage(userMessage)

        val chat = chatRepository.getChat(chatId)
        val messages = chatRepository.getMessages(chatId)
        val isFirstUserMessage = messages.count { it.author == Message.Author.User } == 1

        if (isFirstUserMessage && chat?.title.isNullOrBlank()) {
            val title = ChatTitleGenerator.fromMessage(userMessage.text)
            if (title.isNotBlank()) {
                chatRepository.renameChat(chatId, title)
            }
        }

        val assistantId = UUID.randomUUID().toString()
        chatRepository.addMessage(
            Message(
                id = assistantId,
                chatId = chatId,
                text = "",
                author = Message.Author.Assistant,
                createdAt = Instant.now(),
                status = Message.Status.Sending
            )
        )

        val history = messages.filter { it.id != assistantId }

        val accumulated = StringBuilder()

        gigaChatRepository.streamCompletion(history, model)
            .catch { e ->
                chatRepository.updateMessageStatus(assistantId, Message.Status.Failed)
                throw e
            }
            .onCompletion { cause ->
                if (cause == null) {
                    chatRepository.updateMessage(
                        id = assistantId,
                        text = accumulated.toString(),
                        status = Message.Status.Sent
                    )
                }
            }
            .collect { chunk ->
                accumulated.append(chunk)
                emit(chunk)
            }
    }
}

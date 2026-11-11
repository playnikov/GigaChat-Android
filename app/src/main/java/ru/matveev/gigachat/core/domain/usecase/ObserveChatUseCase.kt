package ru.matveev.gigachat.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.matveev.gigachat.core.domain.repository.ChatRepository
import ru.matveev.gigachat.core.model.Chat
import javax.inject.Inject

class ObserveChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<Chat?> = chatRepository.observeChat(chatId)
}

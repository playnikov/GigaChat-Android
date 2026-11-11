package ru.matveev.gigachat.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.matveev.gigachat.core.domain.repository.ChatRepository
import ru.matveev.gigachat.core.model.Message
import javax.inject.Inject

class ObserveMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<List<Message>> =
        chatRepository.observeMessages(chatId)
}
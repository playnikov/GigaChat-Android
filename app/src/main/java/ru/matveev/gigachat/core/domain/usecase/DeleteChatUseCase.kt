package ru.matveev.gigachat.core.domain.usecase

import ru.matveev.gigachat.core.domain.repository.ChatRepository
import javax.inject.Inject

class DeleteChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: String) = chatRepository.deleteChat(chatId)
}

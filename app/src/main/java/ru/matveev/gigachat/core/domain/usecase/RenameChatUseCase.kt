package ru.matveev.gigachat.core.domain.usecase

import ru.matveev.gigachat.core.domain.repository.ChatRepository
import javax.inject.Inject

class RenameChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: String, title: String) {
        val normalized = title.trim()
        if (normalized.isEmpty()) return
        chatRepository.renameChat(chatId, normalized)
    }
}

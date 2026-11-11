package ru.matveev.gigachat.core.domain.usecase

import ru.matveev.gigachat.core.domain.repository.ChatRepository
import ru.matveev.gigachat.core.model.Chat
import javax.inject.Inject

class CreateChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(title: String): Chat = chatRepository.createChat(title)
}

package ru.matveev.gigachat.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.matveev.gigachat.core.domain.repository.ChatRepository
import ru.matveev.gigachat.core.model.Chat
import javax.inject.Inject

class ObserveChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<Chat>> = chatRepository.observeChats()
}

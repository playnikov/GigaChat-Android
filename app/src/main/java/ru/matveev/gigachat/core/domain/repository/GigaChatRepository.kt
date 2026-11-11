package ru.matveev.gigachat.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.matveev.gigachat.core.error.AppResult
import ru.matveev.gigachat.core.model.GigaChatModel
import ru.matveev.gigachat.core.model.Message

interface GigaChatRepository {
    suspend fun getModels(): AppResult<List<GigaChatModel>>

    fun streamCompletion(history: List<Message>, model: String): Flow<String>
}

package ru.matveev.gigachat.core.domain.repository

interface ModelPreferencesRepository {
    fun selectedModel(): String
    fun setSelectedModel(modelId: String)
}

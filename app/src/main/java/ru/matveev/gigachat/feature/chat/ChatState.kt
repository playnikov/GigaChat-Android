package ru.matveev.gigachat.feature.chat

import ru.matveev.gigachat.core.model.GigaChatModel
import ru.matveev.gigachat.core.model.Message

data class ChatState(
    val chatId: String = "",
    val title: String = "",
    val isTitleDialogVisible: Boolean = false,
    val titleDraft: String = "",
    val messages: List<Message> = emptyList(),
    val streamingText: String = "",
    val isGenerating: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null,
    val availableModels: List<GigaChatModel> = GigaChatModel.knownModels,
    val selectedModel: String = GigaChatModel.DEFAULT_ID
) {
    val visibleMessages: List<Message>
        get() = messages.filter { it.text.isNotBlank() }
}

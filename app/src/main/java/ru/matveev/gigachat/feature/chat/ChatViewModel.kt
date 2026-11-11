package ru.matveev.gigachat.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.matveev.gigachat.core.domain.usecase.GetGigaChatModelsUseCase
import ru.matveev.gigachat.core.domain.usecase.GetSelectedModelUseCase
import ru.matveev.gigachat.core.domain.usecase.ObserveChatUseCase
import ru.matveev.gigachat.core.domain.usecase.ObserveMessagesUseCase
import ru.matveev.gigachat.core.domain.usecase.RenameChatUseCase
import ru.matveev.gigachat.core.domain.usecase.SendMessageUseCase
import ru.matveev.gigachat.core.domain.usecase.SetSelectedModelUseCase
import ru.matveev.gigachat.core.error.AppResult
import ru.matveev.gigachat.core.model.GigaChatModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val observeChatUseCase: ObserveChatUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val renameChatUseCase: RenameChatUseCase,
    private val getModelsUseCase: GetGigaChatModelsUseCase,
    getSelectedModelUseCase: GetSelectedModelUseCase,
    private val setSelectedModelUseCase: SetSelectedModelUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(
        ChatState(selectedModel = getSelectedModelUseCase())
    )
    val state = _state.asStateFlow()

    private var observeJob: Job? = null

    init {
        loadModels()
    }

    fun openChat(chatId: String) {
        if (_state.value.chatId == chatId) return
        observeJob?.cancel()

        val current = _state.value
        _state.value = ChatState(
            chatId = chatId,
            availableModels = current.availableModels,
            selectedModel = current.selectedModel
        )

        observeJob = viewModelScope.launch {
            combine(
                observeChatUseCase(chatId),
                observeMessagesUseCase(chatId)
            ) { chat, messages -> chat to messages }
                .collect { (chat, messages) ->
                    _state.update {
                        it.copy(
                            title = chat?.title.orEmpty(),
                            messages = messages,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onModelSelected(modelId: String) {
        setSelectedModelUseCase(modelId)
        _state.update { it.copy(selectedModel = modelId) }
    }

    fun onEditTitleClick() = _state.update {
        it.copy(isTitleDialogVisible = true, titleDraft = it.title)
    }

    fun onTitleDraftChange(value: String) = _state.update {
        it.copy(titleDraft = value)
    }

    fun onTitleEditCancel() = _state.update {
        it.copy(isTitleDialogVisible = false, titleDraft = it.title)
    }

    fun onTitleEditSave() {
        val id = _state.value.chatId
        if (id.isBlank()) return
        val title = _state.value.titleDraft.trim()
        if (title.isEmpty()) return

        viewModelScope.launch {
            renameChatUseCase(id, title)
            _state.update { it.copy(isTitleDialogVisible = false) }
        }
    }

    fun sendMessage(text: String) {
        val current = _state.value
        val chatId = current.chatId
        if (chatId.isBlank() || text.isBlank() || current.isGenerating) return

        _state.update { it.copy(isGenerating = true, streamingText = "", error = null) }

        viewModelScope.launch {
            sendMessageUseCase(chatId, text, current.selectedModel)
                .catch { e ->
                    _state.update {
                        it.copy(isGenerating = false, streamingText = "", error = e.message)
                    }
                }
                .collect { chunk ->
                    _state.update { it.copy(streamingText = it.streamingText + chunk) }
                }
            _state.update { it.copy(isGenerating = false, streamingText = "") }
        }
    }

    private fun loadModels() {
        viewModelScope.launch {
            val models = when (val result = getModelsUseCase()) {
                is AppResult.Success -> result.data.ifEmpty { GigaChatModel.knownModels }
                is AppResult.Failure -> GigaChatModel.knownModels
            }

            val currentSelected = _state.value.selectedModel
            val selected = currentSelected
                .takeIf { id -> models.any { it.id == id } }
                ?: models.firstOrNull()?.id
                ?: GigaChatModel.DEFAULT_ID

            if (selected != currentSelected) {
                setSelectedModelUseCase(selected)
            }

            _state.update {
                it.copy(
                    availableModels = models,
                    selectedModel = selected
                )
            }
        }
    }
}

package ru.matveev.gigachat.feature.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.matveev.gigachat.core.domain.usecase.CreateChatUseCase
import ru.matveev.gigachat.core.domain.usecase.DeleteChatUseCase
import ru.matveev.gigachat.core.domain.usecase.ObserveChatsUseCase
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val createChatUseCase: CreateChatUseCase,
    private val deleteChatUseCase: DeleteChatUseCase,
    observeChatsUseCase: ObserveChatsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ChatListState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeChatsUseCase().collect { chats ->
                _state.update { it.copy(chats = chats, isLoading = false) }
            }
        }
    }

    fun onQueryChange(newQuery: String) = _state.update { it.copy(query = newQuery) }

    fun onSearchClick() = _state.update {
        if (it.isSearchVisible) {
            it.copy(isSearchVisible = false, query = "")
        } else {
            it.copy(isSearchVisible = true)
        }
    }

    fun onCreateChat(title: String, onCreated: (String) -> Unit) {
        viewModelScope.launch {
            val chat = createChatUseCase(title)
            onCreated(chat.id)
        }
    }

    fun onDeleteChat(chatId: String) {
        viewModelScope.launch {
            deleteChatUseCase(chatId)
        }
    }
}

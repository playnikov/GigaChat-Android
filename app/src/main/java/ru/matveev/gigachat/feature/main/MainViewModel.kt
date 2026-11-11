package ru.matveev.gigachat.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.matveev.gigachat.core.domain.usecase.CreateChatUseCase
import ru.matveev.gigachat.core.domain.usecase.SignOutUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val createChatUseCase: CreateChatUseCase
) : ViewModel() {
    fun signOut() {
        signOutUseCase()
    }

    fun createChat(title: String, onCreated: (String) -> Unit) {
        viewModelScope.launch {
            val chat = createChatUseCase(title)
            onCreated(chat.id)
        }
    }
}

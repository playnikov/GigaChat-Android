package ru.matveev.gigachat.feature.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.matveev.gigachat.R
import ru.matveev.gigachat.core.model.Message
import ru.matveev.gigachat.core.ui.theme.GigaChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String,
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val visibleMessages = remember(state.messages) {
        state.messages.filter { it.text.isNotBlank() }
    }
    val reversedMessages = remember(visibleMessages) { visibleMessages.asReversed() }
    val isAtBottom by remember(listState) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val firstVisible = layoutInfo.visibleItemsInfo.firstOrNull()?.index
                ?: return@derivedStateOf false
            layoutInfo.totalItemsCount > 0 && firstVisible == 0
        }
    }
    val messageCount = visibleMessages.size

    LaunchedEffect(chatId) {
        viewModel.openChat(chatId)
    }
    LaunchedEffect(messageCount) {
        if (messageCount > 0) {
            listState.animateScrollToItem(0)
        }
    }
    LaunchedEffect(state.streamingText) {
        if (state.isGenerating && isAtBottom) {
            listState.scrollToItem(0)
        }
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.title.ifBlank { stringResource(R.string.title_chat) }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::onEditTitleClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.cd_edit_title)
                        )
                    }
                }
            )
        },
        bottomBar = {
            MessageInput(
                value = input,
                onValueChange = { input = it },
                onSend = {
                    viewModel.sendMessage(input)
                    input = ""
                },
                isSending = state.isGenerating,
                models = state.availableModels,
                selectedModel = state.selectedModel,
                onModelSelected = viewModel::onModelSelected,
                modifier = Modifier.padding(8.dp)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            state.error?.let { error ->
                item(key = "error") {
                    Text(
                        text = stringResource(R.string.chat_error, error),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            if (state.isGenerating) {
                item(key = "streaming") {
                    Text(
                        text = stringResource(R.string.chat_streaming_line, state.streamingText),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }

            items(reversedMessages, key = { it.id }) { message ->
                when (message.author) {
                    Message.Author.User -> MessageUserItem(message = message)
                    Message.Author.Assistant -> MessageAssistantItem(message = message)
                }
            }
        }
    }

    if (state.isTitleDialogVisible) {
        AlertDialog(
            onDismissRequest = viewModel::onTitleEditCancel,
            title = { Text(stringResource(R.string.rename_chat_title)) },
            text = {
                OutlinedTextField(
                    value = state.titleDraft,
                    onValueChange = viewModel::onTitleDraftChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.rename_chat_hint)) },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = viewModel::onTitleEditSave,
                    enabled = state.titleDraft.isNotBlank()
                ) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::onTitleEditCancel) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Preview
@Composable
private fun PreviewChatScreen() {
    GigaChatTheme {
        ChatScreen(
            chatId = "preview",
            onBackClick = {}
        )
    }
}

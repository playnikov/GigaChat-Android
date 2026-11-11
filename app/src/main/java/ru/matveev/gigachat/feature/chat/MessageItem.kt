package ru.matveev.gigachat.feature.chat

import android.content.ClipData
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.model.State
import com.mikepenz.markdown.model.parseMarkdown
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.matveev.gigachat.R
import ru.matveev.gigachat.core.model.Message
import ru.matveev.gigachat.core.ui.format.formatMessageTime
import ru.matveev.gigachat.core.ui.theme.GigaChatTheme
import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun MessageItem(
    message: Message,
    modifier: Modifier = Modifier
) {
    when (message.author) {
        Message.Author.User -> MessageUserItem(message, modifier)
        Message.Author.Assistant -> MessageAssistantItem(message, modifier)
    }
}

@Composable
internal fun MessageUserItem(message: Message, modifier: Modifier = Modifier) {
    val isFailed = message.status == Message.Status.Failed
    val containerColor = if (isFailed) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.primary
    }
    val contentColor = if (isFailed) {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        MaterialTheme.colorScheme.onPrimary
    }
    val timestamp = rememberTimestamp(message.createdAt)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Card(
            modifier = Modifier.widthIn(max = 320.dp),
            shape = RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = containerColor,
                contentColor = contentColor
            )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge
                )

                MessageMeta(
                    text = message.text,
                    timestamp = timestamp,
                    isFailed = isFailed,
                    tint = LocalContentColor.current,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
internal fun MessageAssistantItem(message: Message, modifier: Modifier = Modifier) {
    val isFailed = message.status == Message.Status.Failed
    val textColor = if (isFailed) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val timestamp = rememberTimestamp(message.createdAt)
    val markdownState = remember(message.text) { parseMarkdown(message.text) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        if (isFailed || markdownState is State.Error) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Markdown(
                state = markdownState,
                colors = chatMarkdownColors(),
                typography = chatMarkdownTypography(),
                padding = chatMarkdownPadding(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        MessageMeta(
            text = message.text,
            timestamp = timestamp,
            isFailed = isFailed,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun MessageMeta(
    text: String,
    timestamp: String,
    isFailed: Boolean,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isFailed) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = stringResource(R.string.cd_message_failed),
                modifier = Modifier.size(12.dp),
                tint = tint
            )
            Spacer(Modifier.width(4.dp))
        }

        Text(
            text = timestamp,
            style = MaterialTheme.typography.labelSmall,
            color = tint.copy(alpha = 0.7f)
        )

        Spacer(Modifier.width(2.dp))

        CopyButton(text = text, tint = tint.copy(alpha = 0.7f))
    }
}

@Composable
private fun CopyButton(text: String, tint: Color, modifier: Modifier = Modifier) {
    val clipboardManager = LocalClipboard.current
    val hapticFeedback = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    var copied by remember { mutableStateOf(false) }

    LaunchedEffect(copied) {
        if (copied) {
            delay(COPIED_FEEDBACK_MILLIS.milliseconds)
            copied = false
        }
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable {
                scope.launch {
                    val clipData = ClipData.newPlainText("message", text)
                    clipboardManager.setClipEntry(clipData.toClipEntry())
                }
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                copied = true
            }
            .padding(4.dp)
    ) {
        AnimatedContent(
            targetState = copied,
            transitionSpec = {
                (fadeIn() + scaleIn(initialScale = 0.7f)) togetherWith
                    (fadeOut() + scaleOut(targetScale = 0.7f))
            },
            label = "copyIcon"
        ) { isCopied ->
            Icon(
                imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                contentDescription = stringResource(
                    if (isCopied) R.string.cd_message_copied else R.string.cd_copy_message
                ),
                modifier = Modifier.size(14.dp),
                tint = tint
            )
        }
    }
}

@Composable
private fun rememberTimestamp(createdAt: Instant): String =
    remember(createdAt) { formatMessageTime(createdAt) }

private const val COPIED_FEEDBACK_MILLIS = 1_500L

@Preview(showBackground = true)
@Composable
private fun PreviewMessageItemUser() {
    GigaChatTheme {
        MessageItem(
            message = Message(
                id = "user",
                chatId = "chat",
                text = "Привет! Как дела?",
                author = Message.Author.User,
                createdAt = Instant.parse("2026-11-11T18:22:00Z"),
                status = Message.Status.Sent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMessageItemAssistant() {
    GigaChatTheme {
        MessageItem(
            message = Message(
                id = "assistant",
                chatId = "chat",
                text = "Привет! Всё отлично, спасибо. Чем могу помочь?",
                author = Message.Author.Assistant,
                createdAt = Instant.parse("2026-11-11T18:23:00Z"),
                status = Message.Status.Sent
            )
        )
    }
}

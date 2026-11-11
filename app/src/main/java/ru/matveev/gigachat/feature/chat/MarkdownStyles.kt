package ru.matveev.gigachat.feature.chat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.MarkdownColors
import com.mikepenz.markdown.model.MarkdownPadding
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.markdownPadding

@Composable
internal fun chatMarkdownColors(): MarkdownColors = markdownColor(
    text = MaterialTheme.colorScheme.onSurface,
    codeBackground = MaterialTheme.colorScheme.surfaceVariant,
    inlineCodeBackground = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
    dividerColor = MaterialTheme.colorScheme.outlineVariant,
    tableBackground = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
)

@Composable
internal fun chatMarkdownTypography(): MarkdownTypography {
    val body = MaterialTheme.typography.bodyLarge.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
    val code = MaterialTheme.typography.bodyMedium.copy(
        fontFamily = FontFamily.Monospace,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )

    return markdownTypography(
        h1 = body.copy(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold),
        h2 = body.copy(fontSize = 20.sp, lineHeight = 26.sp, fontWeight = FontWeight.Bold),
        h3 = body.copy(fontSize = 18.sp, lineHeight = 24.sp, fontWeight = FontWeight.SemiBold),
        h4 = body.copy(fontSize = 17.sp, lineHeight = 24.sp, fontWeight = FontWeight.SemiBold),
        h5 = body.copy(fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold),
        h6 = body.copy(fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.Medium),
        text = body,
        paragraph = body,
        ordered = body,
        bullet = body,
        list = body,
        quote = body.copy(fontStyle = FontStyle.Italic),
        code = code,
        inlineCode = body.copy(
            fontFamily = FontFamily.Monospace,
            fontSize = 15.sp
        ),
        table = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        textLink = TextLinkStyles(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                textDecoration = TextDecoration.Underline
            )
        ),
        alertTitle = body.copy(fontWeight = FontWeight.Bold)
    )
}

@Composable
internal fun chatMarkdownPadding(): MarkdownPadding = markdownPadding(
    block = 6.dp,
    list = 4.dp,
    listItemTop = 2.dp,
    listItemBottom = 2.dp,
    listIndent = 12.dp,
    codeBlock = PaddingValues(10.dp),
    blockQuote = PaddingValues(horizontal = 12.dp),
    blockQuoteText = PaddingValues(vertical = 2.dp)
)

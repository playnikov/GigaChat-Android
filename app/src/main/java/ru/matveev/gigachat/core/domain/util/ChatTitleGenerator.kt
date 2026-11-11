package ru.matveev.gigachat.core.domain.util

object ChatTitleGenerator {
    private const val MAX_WORDS = 5
    private const val MAX_LENGTH = 40

    fun fromMessage(text: String): String {
        val normalized = text
            .replace(Regex("```[\\s\\S]*?```"), " ")
            .replace(Regex("!?\\[([^]]*)]\\([^)]*\\)"), "$1")
            .replace(Regex("[#*_>`~\\[\\]()]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()

        if (normalized.isEmpty()) return ""

        val words = normalized.split(' ').filter { it.isNotBlank() }
        if (words.isEmpty()) return ""

        val joined = words.take(MAX_WORDS).joinToString(" ")
        val candidate = if (joined.length <= MAX_LENGTH) {
            joined
        } else {
            joined.take(MAX_LENGTH).substringBeforeLast(' ').ifBlank {
                joined.take(MAX_LENGTH)
            }
        }

        return candidate
            .trim()
            .trimEnd('.', ',', ':', ';', '!', '?', '-', '—', '"', '\'')
            .replaceFirstChar { it.uppercase() }
    }
}

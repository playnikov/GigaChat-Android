package ru.matveev.gigachat.core.domain.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ChatTitleGeneratorTest {

    @Test
    fun usesFirstFiveWords() {
        val title = ChatTitleGenerator.fromMessage("раз два три четыре пять шесть семь")

        assertEquals("Раз два три четыре пять", title)
    }

    @Test
    fun trimsTrailingPunctuationAndCapitalizes() {
        val title = ChatTitleGenerator.fromMessage("  привет, как дела?  ")

        assertEquals("Привет, как дела", title)
    }

    @Test
    fun removesCodeBlocks() {
        val title = ChatTitleGenerator.fromMessage(
            "```kotlin\nval answer = 42\n```\nОбъясни, пожалуйста, этот код"
        )

        assertEquals("Объясни, пожалуйста, этот код", title)
    }

    @Test
    fun limitsTitleLength() {
        val title = ChatTitleGenerator.fromMessage(
            "оченьдлинноесловобезпробеловкотороеневозможнообрезатьпосередине"
        )

        assertTrue(title.length <= 40)
    }

    @Test
    fun returnsEmptyForBlankInput() {
        assertEquals("", ChatTitleGenerator.fromMessage("   \n\t  "))
    }
}

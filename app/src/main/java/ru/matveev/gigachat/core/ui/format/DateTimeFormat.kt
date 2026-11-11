package ru.matveev.gigachat.core.ui.format

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM HH:mm")
private val fullDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
private val shortDateFormatter = DateTimeFormatter.ofPattern("dd.MM")
private val shortFullDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

fun formatMessageTime(
    instant: Instant,
    zone: ZoneId = ZoneId.systemDefault()
): String {
    val dateTime = instant.atZone(zone)
    val today = LocalDate.now(zone)
    return when {
        dateTime.toLocalDate() == today -> timeFormatter.format(dateTime)
        dateTime.year == today.year -> dateTimeFormatter.format(dateTime)
        else -> fullDateTimeFormatter.format(dateTime)
    }
}

fun formatChatTime(
    instant: Instant,
    zone: ZoneId = ZoneId.systemDefault()
): String {
    val dateTime = instant.atZone(zone)
    val today = LocalDate.now(zone)
    return when {
        dateTime.toLocalDate() == today -> timeFormatter.format(dateTime)
        dateTime.year == today.year -> shortDateFormatter.format(dateTime)
        else -> shortFullDateFormatter.format(dateTime)
    }
}

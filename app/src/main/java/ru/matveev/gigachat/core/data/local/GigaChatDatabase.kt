package ru.matveev.gigachat.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.matveev.gigachat.core.data.local.dao.ChatDao
import ru.matveev.gigachat.core.data.local.dao.MessageDao
import ru.matveev.gigachat.core.data.local.entity.ChatEntity
import ru.matveev.gigachat.core.data.local.entity.MessageEntity

@Database(
    entities = [ChatEntity::class, MessageEntity::class],
    version = 2,
    exportSchema = false
)
abstract class GigaChatDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
}

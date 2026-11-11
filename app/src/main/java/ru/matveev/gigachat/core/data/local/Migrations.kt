package ru.matveev.gigachat.core.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = Migration(1, 2) { db: SupportSQLiteDatabase ->
    db.execSQL("CREATE INDEX IF NOT EXISTS `index_chats_updatedAt` ON `chats` (`updatedAt`)")
    db.execSQL(
        """
        CREATE TABLE IF NOT EXISTS `messages` (
            `id` TEXT NOT NULL,
            `chatId` TEXT NOT NULL,
            `text` TEXT NOT NULL,
            `author` TEXT NOT NULL,
            `createdAt` INTEGER NOT NULL,
            `status` TEXT NOT NULL,
            PRIMARY KEY(`id`),
            FOREIGN KEY(`chatId`) REFERENCES `chats`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
        )
        """.trimIndent()
    )
    db.execSQL("CREATE INDEX IF NOT EXISTS `index_messages_chatId` ON `messages` (`chatId`)")
}

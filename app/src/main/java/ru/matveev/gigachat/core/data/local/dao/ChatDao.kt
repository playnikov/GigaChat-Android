package ru.matveev.gigachat.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.matveev.gigachat.core.data.local.entity.ChatEntity
import ru.matveev.gigachat.core.data.local.model.ChatWithLastMessage

@Dao
interface ChatDao {

    @Query(
        """
        SELECT chats.*,
            (SELECT messages.text FROM messages
             WHERE messages.chatId = chats.id AND messages.text != ''
             ORDER BY messages.createdAt DESC, messages.id DESC LIMIT 1) AS lastMessageText,
            (SELECT messages.createdAt FROM messages
             WHERE messages.chatId = chats.id AND messages.text != ''
             ORDER BY messages.createdAt DESC, messages.id DESC LIMIT 1) AS lastMessageAt
        FROM chats
        ORDER BY chats.updatedAt DESC, chats.id DESC
        """
    )
    fun observeAllWithLastMessage(): Flow<List<ChatWithLastMessage>>

    @Query("SELECT * FROM chats WHERE id = :id")
    fun observeById(id: String): Flow<ChatEntity?>

    @Query("SELECT * FROM chats WHERE id = :id")
    suspend fun getById(id: String): ChatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(chat: ChatEntity)

    @Query("UPDATE chats SET title = :title WHERE id = :id")
    suspend fun rename(id: String, title: String)

    @Query("DELETE FROM chats WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE chats SET updatedAt = :timestamp WHERE id = :id")
    suspend fun touch(id: String, timestamp: Long)
}

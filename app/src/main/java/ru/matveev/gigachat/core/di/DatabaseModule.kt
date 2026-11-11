package ru.matveev.gigachat.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.matveev.gigachat.core.data.local.GigaChatDatabase
import ru.matveev.gigachat.core.data.local.MIGRATION_1_2
import ru.matveev.gigachat.core.data.local.dao.ChatDao
import ru.matveev.gigachat.core.data.local.dao.MessageDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GigaChatDatabase =
        Room.databaseBuilder(
            context = context,
            klass = GigaChatDatabase::class.java,
            name = "gigachat.db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()

    @Provides
    fun provideChatDao(db: GigaChatDatabase): ChatDao = db.chatDao()

    @Provides
    fun provideMessageDao(db: GigaChatDatabase): MessageDao = db.messageDao()
}

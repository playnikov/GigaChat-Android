package ru.matveev.gigachat.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.matveev.gigachat.core.data.auth.AuthRepositoryImpl
import ru.matveev.gigachat.core.data.chat.ChatRepositoryImpl
import ru.matveev.gigachat.core.data.gigachat.GigaChatRepositoryImpl
import ru.matveev.gigachat.core.data.settings.ModelPreferencesRepositoryImpl
import ru.matveev.gigachat.core.domain.repository.AuthRepository
import ru.matveev.gigachat.core.domain.repository.ChatRepository
import ru.matveev.gigachat.core.domain.repository.GigaChatRepository
import ru.matveev.gigachat.core.domain.repository.ModelPreferencesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindGigaChatRepository(impl: GigaChatRepositoryImpl): GigaChatRepository

    @Binds
    @Singleton
    abstract fun bindModelPreferencesRepository(
        impl: ModelPreferencesRepositoryImpl
    ): ModelPreferencesRepository
}

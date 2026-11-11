package ru.matveev.gigachat.core.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.matveev.gigachat.core.data.auth.google.GoogleTokenProvider

@EntryPoint
@InstallIn(ActivityComponent::class)
interface GoogleAuthEntryPoint {
    fun googleTokenProvider(): GoogleTokenProvider
}

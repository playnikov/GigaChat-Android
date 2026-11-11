package ru.matveev.gigachat.core.di.network

import ru.matveev.gigachat.core.model.GigaChatModel

data class GigaChatConfig(
    val authKey: String,
    val authUrl: String,
    val baseUrl: String,
    val model: String = GigaChatModel.DEFAULT_ID
)

package ru.matveev.gigachat.core.data.gigachat

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.sse.SSEClientException
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.serialization.json.Json
import ru.matveev.gigachat.core.data.gigachat.dto.GigaChatCompletionChunk
import ru.matveev.gigachat.core.data.gigachat.dto.GigaChatCompletionRequest
import ru.matveev.gigachat.core.data.gigachat.dto.GigaChatModelsResponse
import ru.matveev.gigachat.core.data.gigachat.dto.toDomain
import ru.matveev.gigachat.core.data.gigachat.dto.toDto
import ru.matveev.gigachat.core.domain.repository.GigaChatRepository
import ru.matveev.gigachat.core.error.AppResult
import ru.matveev.gigachat.core.error.DomainError
import ru.matveev.gigachat.core.model.GigaChatModel
import ru.matveev.gigachat.core.model.Message
import ru.matveev.gigachat.core.di.network.GigaChatConfig
import java.io.IOException
import javax.inject.Inject

class GigaChatRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val tokenProvider: GigaChatTokenProvider,
    private val config: GigaChatConfig,
    private val json: Json
) : GigaChatRepository {

    override suspend fun getModels(): AppResult<List<GigaChatModel>> = try {
        val token = tokenProvider.getAccessToken()
        val response: GigaChatModelsResponse = httpClient.get("${config.baseUrl}/models") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
        AppResult.Success(response.data.map { it.toDomain() })
    } catch (e: Exception) {
        AppResult.Failure(e.toDomainError())
    }

    override fun streamCompletion(history: List<Message>, model: String): Flow<String> = channelFlow {
        try {
            val token = tokenProvider.getAccessToken()
            val request = GigaChatCompletionRequest(
                model = model,
                messages = history.map { it.toDto() },
                stream = true
            )

            httpClient.sse(
                urlString = "${config.baseUrl}/chat/completions",
                request = {
                    method = HttpMethod.Post
                    header(HttpHeaders.Authorization, "Bearer $token")
                    accept(ContentType.Text.EventStream)
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            ) {
                incoming
                    .takeWhile { event -> event.data != DONE_MARKER }
                    .collect { event ->
                        val payload = event.data ?: return@collect

                        val chunk = runCatching {
                            json.decodeFromString(GigaChatCompletionChunk.serializer(), payload)
                        }.getOrNull() ?: return@collect

                        val content = chunk.choices.firstOrNull()?.delta?.content
                        if (!content.isNullOrEmpty()) {
                            trySend(content)
                        }
                    }
            }
        } catch (e: SSEClientException) {
            val response = e.response
            throw IllegalStateException(
                "GigaChat SSE error (${response?.status?.value}): ${e.message} (${response?.call?.request?.url})",
                e
            )
        } catch (e: ResponseException) {
            throw IllegalStateException(
                "GigaChat HTTP ${e.response.status.value} (${e.response.call.request.url})",
                e
            )
        }
    }

    private fun Throwable.toDomainError(): DomainError = when (this) {
        is ResponseException -> DomainError.Unknown(
            "HTTP ${response.status.value} (${response.call.request.url})"
        )
        is IOException -> DomainError.Network
        else -> DomainError.Unknown(message ?: "Ошибка запроса к GigaChat")
    }

    private companion object {
        const val DONE_MARKER = "[DONE]"
    }
}

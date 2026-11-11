package ru.matveev.gigachat.core.data.gigachat

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.matveev.gigachat.core.data.gigachat.dto.GigaChatAuthResponse
import ru.matveev.gigachat.core.di.network.GigaChatConfig
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GigaChatTokenProvider @Inject constructor(
    private val httpClient: HttpClient,
    private val config: GigaChatConfig
) {
    private val mutex = Mutex()

    @Volatile private var accessToken: String? = null
    @Volatile private var expiresAtMillis: Long = 0L

    suspend fun getAccessToken(): String = mutex.withLock {
        accessToken?.let { token ->
            if (System.currentTimeMillis() + EXPIRY_MARGIN_MILLIS < expiresAtMillis) {
                return@withLock token
            }
        }

        val response = httpClient.post(config.authUrl) {
            header(HttpHeaders.Authorization, "Basic ${config.authKey}")
            header("RqUID", UUID.randomUUID().toString())
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.FormUrlEncoded)
            setBody("scope=GIGACHAT_API_PERS")
        }

        check(response.status.isSuccess()) {
            "GigaChat auth failed: ${response.status} ${response.bodyAsText().take(300)}"
        }

        val body: GigaChatAuthResponse = response.body()

        accessToken = body.accessToken
        expiresAtMillis = body.expiresAt.toEpochMillis()
        body.accessToken
    }

    private fun Long.toEpochMillis(): Long =
        if (this < MILLIS_THRESHOLD) this * 1_000 else this

    private companion object {
        const val EXPIRY_MARGIN_MILLIS = 60_000L
        const val MILLIS_THRESHOLD = 1_000_000_000_000L
    }
}

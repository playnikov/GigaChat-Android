package ru.matveev.gigachat.core.data.gigachat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.matveev.gigachat.core.model.GigaChatModel

@Serializable
data class GigaChatModelsResponse(
    val data: List<GigaChatModelDto> = emptyList()
)

@Serializable
data class GigaChatModelDto(
    val id: String,
    @SerialName("object") val objectType: String? = null,
    @SerialName("owned_by") val ownedBy: String? = null,
    @SerialName("type") val type: String? = null
)

fun GigaChatModelDto.toDomain(): GigaChatModel = GigaChatModel(
    id = id,
    type = type.orEmpty()
)

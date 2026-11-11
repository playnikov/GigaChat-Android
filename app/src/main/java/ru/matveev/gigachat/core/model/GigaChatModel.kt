package ru.matveev.gigachat.core.model

data class GigaChatModel(
    val id: String,
    val type: String = ""
) {
    companion object {
        const val DEFAULT_ID = "GigaChat-2"
        const val TYPE_CHAT = "chat"

        val knownModels: List<GigaChatModel> = listOf(
            GigaChatModel(DEFAULT_ID, TYPE_CHAT),
            GigaChatModel("GigaChat-2-Pro", TYPE_CHAT),
            GigaChatModel("GigaChat-2-Max", TYPE_CHAT),
            GigaChatModel("GigaChat-3-Ultra", TYPE_CHAT)
        )
    }
}

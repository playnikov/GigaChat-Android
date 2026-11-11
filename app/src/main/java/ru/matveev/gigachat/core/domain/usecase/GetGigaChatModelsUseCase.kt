package ru.matveev.gigachat.core.domain.usecase

import ru.matveev.gigachat.core.domain.repository.GigaChatRepository
import ru.matveev.gigachat.core.error.AppResult
import ru.matveev.gigachat.core.model.GigaChatModel
import javax.inject.Inject

class GetGigaChatModelsUseCase @Inject constructor(
    private val gigaChatRepository: GigaChatRepository
) {
    suspend operator fun invoke(): AppResult<List<GigaChatModel>> =
        when (val result = gigaChatRepository.getModels()) {
            is AppResult.Success -> AppResult.Success(
                result.data.filter { model ->
                    model.type.equals(GigaChatModel.TYPE_CHAT, ignoreCase = true)
                }
            )
            is AppResult.Failure -> result
        }
}

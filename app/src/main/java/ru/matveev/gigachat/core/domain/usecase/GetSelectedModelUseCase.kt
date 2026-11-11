package ru.matveev.gigachat.core.domain.usecase

import ru.matveev.gigachat.core.domain.repository.ModelPreferencesRepository
import javax.inject.Inject

class GetSelectedModelUseCase @Inject constructor(
    private val modelPreferencesRepository: ModelPreferencesRepository
) {
    operator fun invoke(): String = modelPreferencesRepository.selectedModel()
}

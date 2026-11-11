package ru.matveev.gigachat.core.data.settings

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.matveev.gigachat.core.domain.repository.ModelPreferencesRepository
import ru.matveev.gigachat.core.model.GigaChatModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModelPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : ModelPreferencesRepository {
    private val preferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun selectedModel(): String =
        preferences.getString(KEY_SELECTED_MODEL, GigaChatModel.DEFAULT_ID)
            ?: GigaChatModel.DEFAULT_ID

    override fun setSelectedModel(modelId: String) {
        preferences.edit().putString(KEY_SELECTED_MODEL, modelId).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "gigachat_settings"
        const val KEY_SELECTED_MODEL = "selected_model"
    }
}

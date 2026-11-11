package ru.matveev.gigachat.core.data.auth.google

import android.app.Activity
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import ru.matveev.gigachat.R
import ru.matveev.gigachat.core.error.AppResult
import ru.matveev.gigachat.core.error.DomainError
import javax.inject.Inject

class GoogleTokenProvider @Inject constructor() {

    suspend fun getIdToken(activity: Activity): AppResult<String> = try {
        val credentialManager = CredentialManager.create(activity)

        val option = GetGoogleIdOption.Builder()
            .setServerClientId(activity.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(option)
            .build()

        val response = credentialManager.getCredential(
            context = activity,
            request = request,
        )
        extractToken(response.credential)
    } catch (e: Exception) {
        AppResult.Failure(e.toGoogleDomainError())
    }

    private fun extractToken(credential: Credential): AppResult<String> {
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)
            return AppResult.Success(googleIdToken.idToken)
        }
        return AppResult.Failure(DomainError.InvalidGoogleToken)
    }
}

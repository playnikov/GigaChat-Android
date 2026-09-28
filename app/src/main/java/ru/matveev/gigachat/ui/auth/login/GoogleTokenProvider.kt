package ru.matveev.gigachat.ui.auth.login

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.matveev.gigachat.R

class GoogleTokenProvider @Inject constructor() {

    suspend fun getIdToken(activity: Activity): Result<String> =
        withContext(Dispatchers.IO) {
            val credentialManager = CredentialManager.create(activity)

            try {
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
            } catch (e: NoCredentialException) {
                getIdTokenWithoutFilter(credentialManager, activity)
            } catch (e: GetCredentialException) {
                Result.failure(e)
            } catch (e: GoogleIdTokenParsingException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    private suspend fun getIdTokenWithoutFilter(
        credentialManager: CredentialManager,
        activity: Activity,
    ): Result<String> = try {
        val option = GetGoogleIdOption.Builder()
            .setServerClientId(activity.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false)
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
        Result.failure(e)
    }

    private fun extractToken(
        credential: androidx.credentials.Credential,
    ): Result<String> {
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)
            return Result.success(googleIdToken.idToken)
        }
        return Result.failure(IllegalStateException("Unexpected credential type"))
    }
}
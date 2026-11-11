package ru.matveev.gigachat.core.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import ru.matveev.gigachat.core.domain.repository.AuthRepository
import ru.matveev.gigachat.core.domain.state.AuthState
import ru.matveev.gigachat.core.error.AppResult
import ru.matveev.gigachat.core.model.User
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override val currentUser: User?
        get() = firebaseAuth.currentUser?.toUser()

    private val _authState = MutableStateFlow<AuthState>(
        if (firebaseAuth.currentUser != null) AuthState.Authenticated
        else AuthState.Unauthenticated
    )
    override val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        firebaseAuth.addAuthStateListener { auth ->
            _authState.value = if (auth.currentUser != null) {
                AuthState.Authenticated
            } else {
                AuthState.Unauthenticated
            }
        }
    }

    override suspend fun signIn(email: String, password: String): AppResult<Unit> =
        runCatchingAuth {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        }

    override suspend fun signUp(email: String, password: String): AppResult<Unit> =
        runCatchingAuth {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        }

    override suspend fun signInWithGoogle(idToken: String): AppResult<Unit> =
        runCatchingAuth {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
        }

    override fun signOut(): AppResult<Unit> = runCatchingAuth {
        firebaseAuth.signOut()
    }

    private inline fun runCatchingAuth(block: () -> Unit): AppResult<Unit> = try {
        block()
        AppResult.Success(Unit)
    } catch (e: Exception) {
        AppResult.Failure(e.toDomainError())
    }
}

private fun FirebaseUser.toUser(): User = User(
    id = uid,
    email = email,
    displayName = displayName
)

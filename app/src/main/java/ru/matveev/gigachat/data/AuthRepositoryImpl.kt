package ru.matveev.gigachat.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import ru.matveev.gigachat.domain.repository.AuthRepository
import ru.matveev.gigachat.domain.state.AuthState
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

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

    override suspend fun signIn(email: String, password: String): AuthState =
        runCatchingAuth {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthState.Authenticated
        }

    override suspend fun signUp(email: String, password: String): AuthState =
        runCatchingAuth {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            AuthState.Authenticated
        }

    override suspend fun signInWithGoogle(idToken: String): AuthState =
        runCatchingAuth {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
            AuthState.Authenticated
        }

    override fun signOut(): AuthState = runCatchingAuth {
        firebaseAuth.signOut()
        AuthState.Unauthenticated
    }

    private inline fun runCatchingAuth(block: () -> AuthState): AuthState = try {
        block()
    } catch (e: Exception) {
        AuthState.Error(e.toDomainError())
    }
}
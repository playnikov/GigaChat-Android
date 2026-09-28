package ru.matveev.gigachat.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.matveev.gigachat.data.AuthRepositoryImpl
import ru.matveev.gigachat.domain.repository.AuthRepository
import ru.matveev.gigachat.domain.use_case.GetCurrentUseCase
import ru.matveev.gigachat.domain.use_case.SignInUseCase
import ru.matveev.gigachat.domain.use_case.SignInWithGoogleUseCase
import ru.matveev.gigachat.domain.use_case.SignUpUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    @Singleton
    fun provideSignInUser(authRepository: AuthRepository): SignInUseCase =
        SignInUseCase(authRepository)

    @Provides
    @Singleton
    fun provideSignUpUser(authRepository: AuthRepository): SignUpUseCase =
        SignUpUseCase(authRepository)

    @Provides
    @Singleton
    fun provideSignInWithGoogleUseCase(
        authRepository: AuthRepository,
    ): SignInWithGoogleUseCase = SignInWithGoogleUseCase(authRepository)

    @Provides
    @Singleton
    fun provideGetCurrentUser(authRepository: AuthRepository): GetCurrentUseCase =
        GetCurrentUseCase(authRepository)
}
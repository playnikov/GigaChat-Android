package ru.matveev.gigachat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import ru.matveev.gigachat.feature.auth.signin.SignInScreen
import ru.matveev.gigachat.feature.auth.signup.SignUpScreen

@Composable
fun NavigationAuth(
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rootBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.Auth.SignIn::class, Route.Auth.SignIn.serializer())
                    subclass(Route.Auth.SignUp::class, Route.Auth.SignUp.serializer())
                }
            }
        },
        Route.Auth.SignIn
    )

    NavDisplay(
        modifier = modifier,
        backStack = rootBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.Auth.SignIn> {
                SignInScreen(
                    onSuccess = onSuccess,
                    onSignUp = {
                        rootBackStack.add(Route.Auth.SignUp)
                    }
                )
            }

            entry<Route.Auth.SignUp> {
                SignUpScreen(
                    onSuccess = onSuccess,
                    onSignIn = {
                        rootBackStack.remove(Route.Auth.SignUp)
                    }
                )
            }
        }
    )
}

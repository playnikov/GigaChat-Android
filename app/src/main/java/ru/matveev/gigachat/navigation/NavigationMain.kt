package ru.matveev.gigachat.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import ru.matveev.gigachat.core.ui.component.DrawerContent
import ru.matveev.gigachat.feature.chat.ChatScreen
import ru.matveev.gigachat.feature.chatlist.ChatListScreen
import ru.matveev.gigachat.feature.main.MainViewModel

@Composable
fun NavigationMain(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel: MainViewModel = hiltViewModel()

    val rootBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.Main.ChatList::class, Route.Main.ChatList.serializer())
                    subclass(Route.Main.Chat::class, Route.Main.Chat.serializer())
                    subclass(Route.Main.Profile::class, Route.Main.Profile.serializer())
                }
            }
        },
        Route.Main.ChatList
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = rootBackStack.lastOrNull() == Route.Main.ChatList,
        drawerContent = {
            DrawerContent(
                onCreateChatClick = {
                    viewModel.createChat("") { chatId ->
                        rootBackStack.add(Route.Main.Chat(chatId))
                    }
                    scope.launch { drawerState.close() }
                },
                onImagesClick = {
                    scope.launch { drawerState.close() }
                },
                onChatsClick = {
                    scope.launch { drawerState.close() }
                },
                onProfileClick = {
                    scope.launch { drawerState.close() }
                },
                onSignOutClick = {
                    viewModel.signOut()
                    scope.launch { drawerState.close() }
                    onLogout()
                }
            )
        }
    ) {
        NavDisplay(
            modifier = modifier,
            backStack = rootBackStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Route.Main.ChatList> {
                    ChatListScreen(
                        onOpenDrawer = {
                            scope.launch { drawerState.open() }
                        },
                        onNewChat = { chatId ->
                            rootBackStack.add(Route.Main.Chat(chatId))
                        },
                        onChatClick = { chatId ->
                            rootBackStack.add(Route.Main.Chat(chatId))
                        }
                    )
                }

                entry<Route.Main.Chat> { key ->
                    ChatScreen(
                        chatId = key.chatId,
                        onBackClick = { rootBackStack.removeLastOrNull() }
                    )
                }
            }
        )
    }
}

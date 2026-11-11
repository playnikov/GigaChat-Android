package ru.matveev.gigachat.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.matveev.gigachat.R
import ru.matveev.gigachat.core.ui.theme.GigaChatTheme

@Composable
fun DrawerContent(
    onCreateChatClick: () -> Unit,
    onImagesClick: () -> Unit,
    onChatsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxHeight()
            .width(320.dp)
    ) {
        Text(
            text = stringResource(R.string.menu_name),
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
        )

        HorizontalDivider()

        Spacer(Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(R.string.menu_new_chat),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.cd_create_chat)
                    )
                },
                onClick = onCreateChatClick,
                selected = false,
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )

            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(R.string.menu_images),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = stringResource(R.string.cd_images)
                    )
                },
                onClick = onImagesClick,
                selected = false,
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )

            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(R.string.menu_chats),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.ChatBubble,
                        contentDescription = stringResource(R.string.cd_all_chats)
                    )
                },
                onClick = onChatsClick,
                selected = false,
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )

            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(R.string.menu_profile),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(R.string.cd_profile)
                    )
                },
                onClick = onProfileClick,
                selected = false,
                modifier = Modifier
                    .padding(NavigationDrawerItemDefaults.ItemPadding)
                    .padding(bottom = 16.dp)
            )
        }

        Spacer(Modifier.weight(1f))

        NavigationDrawerItem(
            label = {
                Text(
                    text = stringResource(R.string.menu_logout),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = stringResource(R.string.cd_logout)
                )
            },
            onClick = onSignOutClick,
            selected = false,
            colors = NavigationDrawerItemDefaults.colors(
                unselectedTextColor = MaterialTheme.colorScheme.error,
                unselectedIconColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Preview
@Composable
private fun PreviewDrawerContent(

) {
    GigaChatTheme {
        DrawerContent(
            onChatsClick = {

            },
            onProfileClick = {

            },
            onSignOutClick = {

            },
            onCreateChatClick = {

            },
            onImagesClick = {

            }
        )
    }
}
package ru.matveev.gigachat.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StyledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(size = 15.dp),
        enabled = enabled
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 3.dp)
        ) {
            content()
        }
    }
}
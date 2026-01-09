package io.github.citrusella.iffyoureallyseeeurydice.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun Theme(content: @Composable () -> Unit) {
    val colorScheme = colorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography(),
        shapes = shapes,
        content = content
    )
}
package io.github.citrusella.iffyoureallyseeeurydice.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val background = Color(0x000c4b)
val button = Color(0x00518c)
val textColor = Color(0xc0c7cb)
val hoverColor = Color(0xFFF)

val colorScheme = lightColorScheme(
    primary = background,
    onPrimary = textColor,
    secondary = button,
    onSecondary = hoverColor,
    background = background,
    onBackground = textColor,
)
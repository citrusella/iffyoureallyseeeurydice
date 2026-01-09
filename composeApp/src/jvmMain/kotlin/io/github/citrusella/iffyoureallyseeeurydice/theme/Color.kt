package io.github.citrusella.iffyoureallyseeeurydice.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val background = Color(0xFF000c4b)
val button = Color(0xFF00518c)
val textColor = Color(0xFFc0c7cb)
val hoverColor = Color(0xFFFFFFFF)
val linkColor = Color(0xFFA4D6FF)
val disabledButton = Color(0x8000518c)
val disabledButtonText = Color(0x80c0c7cb)

val colorScheme = lightColorScheme(
    primary = linkColor,
    onPrimary = textColor,
    secondary = button,
    onSecondary = hoverColor,
    background = background,
    onBackground = textColor,
    tertiary = disabledButton,
    onTertiary = disabledButtonText
)
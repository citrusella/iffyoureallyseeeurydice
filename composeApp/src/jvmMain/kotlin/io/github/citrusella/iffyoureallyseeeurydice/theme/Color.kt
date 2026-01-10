package io.github.citrusella.iffyoureallyseeeurydice.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val background = Color(0xFF000c4b)
val button = Color(0xFF00518c)
val textColor = Color(0xFFc0c7cb)
val hoverColor = Color(0xFFFFFFFF)
val linkColor = Color(0xFFA4D6FF)
val disabledButton = Color(0xFF000029)//Color(0x4000518c)
val disabledButtonText = Color(0x80c0c7cb)
val error = Color(0xFFE04850)

val colorScheme = lightColorScheme(
    primary = linkColor,
    onPrimary = textColor,
    secondary = button,
    onSecondary = hoverColor,
    background = background,
    onBackground = textColor,
    tertiary = disabledButton,
    onTertiary = disabledButtonText,
    error = error
)
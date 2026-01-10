package io.github.citrusella.iffyoureallyseeeurydice.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import iffyoureallyseeeurydice.composeapp.generated.resources.ComicNeue_Bold
import iffyoureallyseeeurydice.composeapp.generated.resources.ComicNeue_Regular
import iffyoureallyseeeurydice.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun AppTypography(): Typography {
    val comicSans = FontFamily(
        Font(Res.font.ComicNeue_Bold, FontWeight.Bold),
        Font(Res.font.ComicNeue_Regular, FontWeight.Normal)
    )
    return Typography(
        bodyMedium = TextStyle(
            fontFamily = comicSans,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
}
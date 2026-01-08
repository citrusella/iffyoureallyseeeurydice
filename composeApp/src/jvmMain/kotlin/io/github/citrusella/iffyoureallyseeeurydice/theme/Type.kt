package io.github.citrusella.iffyoureallyseeeurydice.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import iffyoureallyseeeurydice.composeapp.generated.resources.ComicNeue_Bold
import iffyoureallyseeeurydice.composeapp.generated.resources.ComicNeue_Regular
import iffyoureallyseeeurydice.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

val AppTypography = Typography()

@Composable
fun AppTypography(): Typography {
    val comicSans = FontFamily(
        Font(Res.font.ComicNeue_Bold, FontWeight.Bold),
        Font(Res.font.ComicNeue_Regular, FontWeight.Normal)
    )
    return Typography(
        body1 = TextStyle(
            fontFamily = comicSans,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        ),
        h1 = TODO(),
        h2 = TODO(),
        h3 = TODO(),
        h4 = TODO(),
        h5 = TODO(),
        h6 = TODO(),
        subtitle1 = TODO(),
        subtitle2 = TODO(),
        body2 = TODO(),
        button = TODO(),
        caption = TODO(),
        overline = TODO()
    )
}
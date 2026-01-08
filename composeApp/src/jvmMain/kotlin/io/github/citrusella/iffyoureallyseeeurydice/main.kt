package io.github.citrusella.iffyoureallyseeeurydice

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import iffyoureallyseeeurydice.composeapp.generated.resources.Res
import iffyoureallyseeeurydice.composeapp.generated.resources.app_name
import iffyoureallyseeeurydice.composeapp.generated.resources.eurydice_icon_small
import io.github.vinceglb.filekit.FileKit
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

fun main() {
    FileKit.init(appId = "iffyoureallyseeeurydice")
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = stringResource(Res.string.app_name),
            icon = painterResource(Res.drawable.eurydice_icon_small)
        ) {
            App()
        }
    }
}
package app.dreamlightpal.compose

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
actual fun StatusBarColorEffect(darkTheme: Boolean, primary: Color, background: Color, surface: Color) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val systemUiController = rememberSystemUiController()

        SideEffect {
            systemUiController.setStatusBarColor(
                color = Color.Transparent, darkIcons = !darkTheme
            )
        }
    }
}

actual fun Modifier.statusBarPadding(): Modifier = this.statusBarsPadding()
actual fun Modifier.navigationBarPadding(): Modifier = this.navigationBarsPadding()

package app.dreamlightpal.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/* do nothing */
@Composable
actual fun StatusBarColorEffect(darkTheme: Boolean, primary: Color, background: Color, surface: Color) = Unit

actual fun Modifier.statusBarPadding(): Modifier = this
actual fun Modifier.navigationBarPadding(): Modifier = this

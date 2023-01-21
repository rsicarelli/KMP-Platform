package app.dreamlightpal.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
expect fun StatusBarColorEffect(darkTheme: Boolean, primary: Color, background: Color, surface: Color)

expect fun Modifier.statusBarPadding(): Modifier
expect fun Modifier.navigationBarPadding(): Modifier

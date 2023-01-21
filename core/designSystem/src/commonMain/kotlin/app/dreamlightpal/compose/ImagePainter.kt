package app.dreamlightpal.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import app.dreamlightpal.compose.resource.PlatformResource

@Composable
expect fun rememberAsyncImagePainter(url: String): Painter

@Composable
expect fun rememberAsyncImagePainter(platformResource: PlatformResource): Painter

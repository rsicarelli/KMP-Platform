package app.dreamlightpal.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import app.dreamlightpal.compose.resource.PlatformResource
import coil.compose.rememberAsyncImagePainter

@Composable
actual fun rememberAsyncImagePainter(url: String): Painter = rememberAsyncImagePainter(url)

@Composable
actual fun rememberAsyncImagePainter(platformResource: PlatformResource): Painter =
    rememberAsyncImagePainter(platformResource.resId)

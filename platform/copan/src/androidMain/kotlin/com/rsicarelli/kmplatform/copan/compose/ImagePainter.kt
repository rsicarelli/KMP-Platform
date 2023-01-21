package com.rsicarelli.kmplatform.copan.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import coil.compose.rememberAsyncImagePainter
import com.rsicarelli.kmplatform.copan.compose.resource.CopanResource

@Composable
actual fun rememberAsyncImagePainter(url: String): Painter =
    rememberAsyncImagePainter(url)

@Composable
actual fun rememberCopanResourcePainter(copanResource: CopanResource): Painter =
    rememberAsyncImagePainter(copanResource.androidRes)

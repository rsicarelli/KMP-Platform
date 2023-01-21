package com.rsicarelli.kmplatform.copan.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.rsicarelli.kmplatform.copan.compose.resource.CopanResource

@Composable
expect fun rememberAsyncImagePainter(url: String): Painter

@Composable
expect fun rememberCopanResourcePainter(copanResource: CopanResource): Painter

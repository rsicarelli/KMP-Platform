package com.rsicarelli.kmplatform.copan.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.lt.load_the_image.rememberImagePainter
import com.rsicarelli.kmplatform.copan.compose.resource.CopanResource

@Composable
actual fun rememberAsyncImagePainter(url: String): Painter = rememberImagePainter(url)

@Composable
actual fun rememberCopanResourcePainter(copanResource: CopanResource): Painter =
    painterResource(requireNotNull(copanResource.desktopRes))


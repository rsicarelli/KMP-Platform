package app.dreamlightpal.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.ClassLoaderResourceLoader
import androidx.compose.ui.res.loadImageBitmap
import app.dreamlightpal.compose.resource.PlatformResource
import com.lt.load_the_image.rememberImagePainter

@Composable
actual fun rememberAsyncImagePainter(url: String): Painter = rememberImagePainter(url)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun rememberAsyncImagePainter(platformResource: PlatformResource): Painter {
    requireNotNull(platformResource.fileName)
    return rememberImagePainter(
        bitmap = loadImageBitmap(
            inputStream = ClassLoaderResourceLoader().load(platformResource.fileName!!)
        ).asSkiaBitmap()
    )
}

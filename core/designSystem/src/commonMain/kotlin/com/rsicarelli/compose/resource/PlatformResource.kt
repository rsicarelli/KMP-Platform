package app.dreamlightpal.compose.resource

import androidx.compose.runtime.Stable

@Stable
interface PlatformResource {

    val resId: Int?
    val fileName: String?
}

expect val SpikeBackground: PlatformResource

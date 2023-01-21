package app.dreamlightpal.compose.resource

import app.dreamlightpal.designSystem.R

actual val SpikeBackground: PlatformResource
    get() = object : PlatformResource {
        override val resId: Int = R.drawable.background_spike_white
        override val fileName: String? = null
    }

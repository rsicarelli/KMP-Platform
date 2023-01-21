package app.dreamlightpal.compose.resource

actual val SpikeBackground: PlatformResource
    get() = object : PlatformResource {
        override val resId: Int? = null
        override val fileName: String = "images/night_thorns_background.svg"
    }

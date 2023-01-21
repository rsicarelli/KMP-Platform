package com.rsicarelli.kmplatform.copan.compose.resource

import androidx.compose.runtime.Stable

typealias AndroidRes = Int
typealias DesktopRes = String

@Stable
interface CopanResource {

    val androidRes: AndroidRes?
    val desktopRes: DesktopRes?
}

fun CopanResource(
    androidRes: AndroidRes? = null,
    desktopRes: DesktopRes? = null,
): CopanResource {
    require(androidRes != null || desktopRes != null)
    return object : CopanResource {
        override val androidRes: AndroidRes? = androidRes
        override val desktopRes: DesktopRes? = desktopRes
    }
}

@Stable
expect val SpikeBackground: CopanResource






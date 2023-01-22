@file:Suppress("DSL_SCOPE_VIOLATION")

import config.PlatformPublicationTarget

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.rsicarelli.kmplatform)
}

version = libs.versions.kmplatform.get()
description = "Common Multiplatform implementation for logging"

installComponentPublication(PlatformPublicationTarget.Multiplatform)
installMultiplatformLibrary(
    commonMainDependencies = {
        api(libs.touchlab.kermit)
    }
)

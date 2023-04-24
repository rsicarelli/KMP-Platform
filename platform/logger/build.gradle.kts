plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.rsicarelli.kmplatform)
}

version = libs.versions.kmplatform.get()
description = "Common Multiplatform implementation for logging"

installComponentPublication(PlatformPublicationTarget.Multiplatform)
installMultiplatformLibrary(
    multiplatformDependencyHandler = MultiplatformDependencyHandler(
        common = {
            api(libs.touchlab.kermit)
        }
    )
)

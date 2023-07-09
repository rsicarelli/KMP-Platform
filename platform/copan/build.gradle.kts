plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    alias(libs.plugins.rsicarelli.kmplatform)
}

version = libs.versions.kmplatform.get()
description = "Copan is the KMPlatform Design System with common Composable UI implementations."

installComponentPublication(PlatformPublicationTarget.Multiplatform)
installMultiplatformLibrary(
    multiplatformDependencyHandler = MultiplatformDependencyHandler(
        common = {
            api(compose.dependencies.runtime)
            api(compose.dependencies.foundation)
            api(compose.dependencies.material3)
        },
        android = {
            implementation(libs.androidx.core)
            implementation(libs.google.accompanist.systemuicontroller)
            implementation(libs.coil.compose)
        },
        desktop = {
            implementation(libs.load.the.image)
        },
    )
)
installComposeMultiplatform()

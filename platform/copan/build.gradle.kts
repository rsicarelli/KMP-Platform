@file:Suppress("DSL_SCOPE_VIOLATION")

import config.AndroidConfig.AndroidLibraryConfig
import config.AndroidConfig.AndroidLibraryConfig.BuildFeaturesConfig
import config.ComposeConfig
import config.PlatformPublicationTarget

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
    composeConfig = ComposeConfig(
        runtime = true,
        ui = true
    ),
    androidLibraryConfig = AndroidLibraryConfig(
        buildFeaturesConfig = BuildFeaturesConfig(
            generateAndroidResources = true,
            generateResValues = true,
            generateBuildConfig = true
        )
    ),
    commonMainDependencies = {
        api(compose.dependencies.materialIconsExtended)
    },
    androidMainDependencies = {
        compileOnly(libs.androidx.core)
        implementation(libs.google.accompanist.systemuicontroller)
        implementation(libs.coil.compose)
    },
    desktopMainDependencies = {
        implementation(libs.load.the.image)
    },
)

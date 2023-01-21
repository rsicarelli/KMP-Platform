@file:Suppress("DSL_SCOPE_VIOLATION")

import config.AndroidConfig.AndroidLibraryConfig
import config.AndroidConfig.AndroidLibraryConfig.AndroidBuildFeaturesConfig
import config.ComposeConfig

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    alias(libs.plugins.rsicarelli.kmplatform)
}

installMultiplatformLibrary(
    composeConfig = ComposeConfig(
        runtime = true,
        ui = true
    ),
    androidLibraryConfig = AndroidLibraryConfig(
        buildFeaturesConfig = AndroidBuildFeaturesConfig(
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

@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.rsicarelli.kmplatform)
}

installMultiplatformLibrary(
    commonMainDependencies = {
        implementation(libs.touchlab.kermit)
    }
)

@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.rsicarelli.kmplatform)
}

installMultiplatformLibrary(
    commonMainDependencies = {
        api(libs.jetbrains.kotlinx.coroutines.core)
        compileOnly(libs.kodein.di)
    }
)

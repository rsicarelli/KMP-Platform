@file:Suppress("DSL_SCOPE_VIOLATION")

import decorators.ComponentPublication

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.rsicarelli.kmplatform)
}

version = libs.versions.kmplatform.get()
description = "Common multiplatform implementations for Coroutines."

installComponentPublication(ComponentPublication.Multiplatform)
installMultiplatformLibrary(
    commonMainDependencies = {
        api(libs.jetbrains.kotlinx.coroutines.core)
        compileOnly(libs.kodein.di)
    }
)

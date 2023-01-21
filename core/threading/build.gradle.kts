////import decorators.setupMultiplatformLibrary
@file:Suppress("DSL_SCOPE_VIOLATION")

//
plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.rsicarelli.kmpgradleplatform)
}

installMultiplatformLibrary(
    commonMainDependencies = {
        api(libs.jetbrains.kotlinx.coroutines.core)
        compileOnly(libs.kodein.di)
    }
)
////
////setupMultiplatformLibrary(
////    commonMainDependencies = {
////        api(libs.jetbrains.kotlinx.coroutines.core)
////        compileOnly(libs.kodein.di)
////    }
////)

@file:Suppress("UnstableApiUsage")

import org.gradle.api.internal.FeaturePreviews.Feature.STABLE_CONFIGURATION_CACHE
import org.gradle.api.internal.FeaturePreviews.Feature.TYPESAFE_PROJECT_ACCESSORS

enableFeaturePreview(TYPESAFE_PROJECT_ACCESSORS.name)
enableFeaturePreview(STABLE_CONFIGURATION_CACHE.name)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
    plugins {
        val kotlinVersion = extra["kotlin.version"] as String
        val agpVersion = extra["agp.version"] as String
        val composeVersion = extra["compose.wasm.version"] as String
        val detekt = extra["detekt"] as String
        val kmpPlatformVersion = extra["kmp.platform.version"] as String

        kotlin("jvm").version(kotlinVersion)
        kotlin("multiplatform").version(kotlinVersion)
        kotlin("plugin.serialization").version(kotlinVersion)
        kotlin("android").version(kotlinVersion)
        id("com.android.base").version(agpVersion)
        id("com.android.application").version(agpVersion)
        id("com.android.library").version(agpVersion)
        id("org.jetbrains.compose").version(composeVersion)
        id("io.gitlab.arturbosch.detekt").version(detekt)
        id("com.rsicarelli.kmplatform").version(kmpPlatformVersion)
    }

    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven("https://jitpack.io")
    }
}

private object ProjectDefaults {

    const val name: String = "KMP-Platform"

    val coreModules = sequenceOf(
        "copan", "logger", "threading"
    ).map { ":platform:$it" }.asIterable()

    val sharedFiles = sequenceOf(
        "gradle.properties", "gradlew",
        "gradlew.txt", "gradlew.bat",
        "gradle/wrapper/gradle-wrapper.jar",
        "gradle/wrapper/gradle-wrapper.properties"
    )
}

with(ProjectDefaults) {
    rootProject.name = name
    rootDir.run {
        sharedFiles.forEach { path ->
            resolve(path).copyTo(
                rootDir.resolve("build-logic").resolve(path),
                overwrite = true
            )
        }
    }
    include(coreModules)
}

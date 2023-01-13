@file:Suppress("UnstableApiUsage")

import org.gradle.api.internal.FeaturePreviews.Feature.TYPESAFE_PROJECT_ACCESSORS

enableFeaturePreview(TYPESAFE_PROJECT_ACCESSORS.name)

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }

    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
    }
}

private object ProjectDefaults {

    const val name: String = "KMPGradlePlatform"

    val coreModules = sequenceOf(
        "designsystem", "logger", "threading"
    ).map { ":core:$it" }.asIterable()

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

@file:Suppress("UnstableApiUsage")

package decorators

import MultiplatformDependencyHandler
import MultiplatformLibraryConfig
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.withExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

internal fun Project.setMultiplatformLibrary(
    multiplatformLibraryConfig: MultiplatformLibraryConfig,
    multiplatformDependencyHandler: MultiplatformDependencyHandler,
) {
    setupMultiplatformLibrary(
        multiplatformLibraryConfig = multiplatformLibraryConfig,
        multiplatformDependencyHandler = multiplatformDependencyHandler
    )
    setAndroidLibrary(
        libraryConfig = multiplatformLibraryConfig.androidLibraryConfig,
        compilationConfig = multiplatformLibraryConfig.compilationConfig
    )
    setJUnit5()
}

@OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)
private fun KotlinMultiplatformExtension.defineMultiplatformTargets() {
    targetHierarchy.default {
        common {
            withJvm()
            withAndroid()
            withApple()
            withLinux()
        }
        group("jsBased") {
            withJs()
        }
    }
//    wasm {
//        browser()
//    }
}

private fun Project.setupMultiplatformLibrary(
    multiplatformLibraryConfig: MultiplatformLibraryConfig,
    multiplatformDependencyHandler: MultiplatformDependencyHandler,
) {
    extensions.configure<KotlinMultiplatformExtension> {
        defineMultiplatformTargets()
        configureSourceSets(
            multiplatformDependencyHandler = multiplatformDependencyHandler,
            multiplatformLibraryConfig = multiplatformLibraryConfig
        )
        targets.withType<KotlinNativeTarget>().configureEach {
            configureFramework(multiplatformLibraryConfig)
        }
    }


    afterEvaluate {
        withExtension<KotlinMultiplatformExtension> {
            sourceSets.all {
                languageSettings.progressiveMode = true
            }
        }
    }
}

private fun KotlinNativeTarget.configureFramework(multiplatformLibraryConfig: MultiplatformLibraryConfig) {
    binaries.framework {
        isStatic = multiplatformLibraryConfig.iOSConfig.isStatic
        baseName = multiplatformLibraryConfig.iOSConfig.frameworkName

        multiplatformLibraryConfig.iOSConfig.framework(this)
    }
}

private fun KotlinMultiplatformExtension.configureSourceSets(
    multiplatformLibraryConfig: MultiplatformLibraryConfig,
    multiplatformDependencyHandler: MultiplatformDependencyHandler,
) {
    sourceSets {
        val commonMain = configureCommonMainSourceSet(multiplatformDependencyHandler)
        val iosMain = configureIosMainSourceSet(multiplatformDependencyHandler, commonMain)

        configureSpecificSourceSets(multiplatformDependencyHandler, iosMain, commonMain)
        removeIgnoredSourceSets(multiplatformLibraryConfig)
        configureOptInSourceSets(multiplatformLibraryConfig)
    }
}

private fun NamedDomainObjectContainer<KotlinSourceSet>.configureCommonMainSourceSet(
    multiplatformDependencyHandler: MultiplatformDependencyHandler,
    name: String = "commonMain",
): KotlinSourceSet = getByName(name) {
    dependencies(multiplatformDependencyHandler.common)
}

private fun NamedDomainObjectContainer<KotlinSourceSet>.configureIosMainSourceSet(
    multiplatformDependencyHandler: MultiplatformDependencyHandler,
    commonMain: KotlinSourceSet,
    name: String = "iosMain",
): KotlinSourceSet = create(name) {
    dependsOn(commonMain)
    dependencies(multiplatformDependencyHandler.iOS)
}

private fun NamedDomainObjectContainer<KotlinSourceSet>.configureSpecificSourceSets(
    multiplatformDependencyHandler: MultiplatformDependencyHandler,
    iosMain: KotlinSourceSet,
    commonMain: KotlinSourceSet,
) {
    getByName("wasmMain") { dependsOn(commonMain) }
    getByName("androidMain") { dependencies(multiplatformDependencyHandler.android) }
    getByName("desktopMain") { dependencies(multiplatformDependencyHandler.desktop) }
    getByName("iosX64Main") { dependsOn(iosMain) }
    getByName("iosArm64Main") { dependsOn(iosMain) }
    getByName("iosSimulatorArm64Main") { dependsOn(iosMain) }
}

private fun NamedDomainObjectContainer<KotlinSourceSet>.removeIgnoredSourceSets(multiplatformLibraryConfig: MultiplatformLibraryConfig) {
    removeAll {
        multiplatformLibraryConfig.androidLibraryConfig.ignoredSourceSets.contains(it.name)
    }
}

private fun NamedDomainObjectContainer<KotlinSourceSet>.configureOptInSourceSets(
    multiplatformLibraryConfig: MultiplatformLibraryConfig,
) = all {
    languageSettings {
        multiplatformLibraryConfig.compilationConfig.featureOptIns.forEach {
            optIn(it.flag)
        }
    }
}

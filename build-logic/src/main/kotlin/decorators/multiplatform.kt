@file:Suppress("UnstableApiUsage")

package decorators

import MultiplatformDependencyHandler
import MultiplatformLibraryConfig
import org.gradle.api.Project
import org.gradle.api.libs
import org.gradle.api.version
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

internal fun Project.setMultiplatformLibrary(
    multiplatformLibraryConfig: MultiplatformLibraryConfig,
    multiplatformDependencyHandler: MultiplatformDependencyHandler,
) {
    extensions.configure<KotlinMultiplatformExtension> {
        jvmTargets()
        webTargets()
        iOSTargets(multiplatformLibraryConfig)
        sourceSets {
            val commonMain = getByName("commonMain") {
                dependencies(multiplatformDependencyHandler.common)
            }

            val iosMain: KotlinSourceSet = create("iosMain") {
                dependsOn(commonMain)
                dependencies(multiplatformDependencyHandler.iOS)
            }

            getByName("wasmMain") {
                dependsOn(commonMain)
            }

            getByName("androidMain") { dependencies(multiplatformDependencyHandler.android) }
            getByName("desktopMain") { dependencies(multiplatformDependencyHandler.desktop) }
            getByName("iosX64Main") { dependsOn(iosMain) }
            getByName("iosArm64Main") { dependsOn(iosMain) }
            getByName("iosSimulatorArm64Main") { dependsOn(iosMain) }

            removeAll {
                multiplatformLibraryConfig.androidLibraryConfig.ignoredSourceSets.contains(it.name)
            }

            all {
                languageSettings {
                    multiplatformLibraryConfig.compilationConfig.featureOptIns.forEach {
                        optIn(it.flag)
                    }
                }
            }
        }
    }

    setAndroidLibrary(
        libraryConfig = multiplatformLibraryConfig.androidLibraryConfig,
        compilationConfig = multiplatformLibraryConfig.compilationConfig
    )
    setJUnit5()
}

private fun KotlinMultiplatformExtension.jvmTargets() {
    android()
    jvm("desktop")
}

@OptIn(ExperimentalWasmDsl::class)
private fun KotlinMultiplatformExtension.webTargets() {
    js(IR) {
        browser()
    }
    wasm {
        browser()
    }
}

private fun KotlinMultiplatformExtension.iOSTargets(multiplatformLibraryConfig: MultiplatformLibraryConfig) {
    iosX64 {
        binaries.framework {
            multiplatformLibraryConfig.iOSConfig.framework
        }
    }
    iosArm64 {
        binaries.framework {
            multiplatformLibraryConfig.iOSConfig.framework
        }
    }
    iosSimulatorArm64 {
        binaries.framework {
            multiplatformLibraryConfig.iOSConfig.framework
        }
    }
}

internal fun Project.setComposeMultiplatform() {
    extensions.configure<ComposeExtension> {
        kotlinCompilerPlugin.set(libs.version("jetbrainsComposeWasm"))
        kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=${libs.version("kotlin")}")
    }
}

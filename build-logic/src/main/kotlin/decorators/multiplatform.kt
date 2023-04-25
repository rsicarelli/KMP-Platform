@file:Suppress("UnstableApiUsage")

package decorators

import ComposeConfig
import MultiplatformDependencyHandler
import MultiplatformLibraryConfig
import org.gradle.api.Project
import org.gradle.api.compose
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

@OptIn(ExperimentalWasmDsl::class)
internal fun Project.setMultiplatformLibrary(
    multiplatformLibraryConfig: MultiplatformLibraryConfig,
    multiplatformDependencyHandler: MultiplatformDependencyHandler,
) {
    extensions.configure<KotlinMultiplatformExtension> {
        android()
        jvm("desktop")
        js(IR) {
            browser()
        }

        wasm {
            browser()
        }
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

        sourceSets {
            val commonMain = getByName("commonMain") { dependencies(multiplatformDependencyHandler.common) }
            val iosMain: KotlinSourceSet = create("iosMain") {
                dependsOn(commonMain)
                dependencies(multiplatformDependencyHandler.iOS)
            }
            val jsWasmMain: KotlinSourceSet = create("jsWasmMain") { dependsOn(commonMain) }
            getByName("jsMain") { dependsOn(jsWasmMain) }
            getByName("wasmMain") { dependsOn(jsWasmMain) }
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

            rootProject.the<NodeJsRootExtension>().versions.webpack.version = "5.76.2"
        }
    }

    multiplatformLibraryConfig.composeConfig?.let(::setComposeMultiplatform)
    setAndroidLibrary(
        libraryConfig = multiplatformLibraryConfig.androidLibraryConfig,
        compilationConfig = multiplatformLibraryConfig.compilationConfig
    )
    setJUnit5()
}

private fun Project.setComposeMultiplatform(composeConfig: ComposeConfig) {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets {
            named("commonMain") {
                dependencies {
                    if (composeConfig.runtime)
                        implementation(compose.dependencies.runtime)
                    if (composeConfig.ui) {
                        implementation(compose.dependencies.foundation)
                        implementation(compose.dependencies.material3)
                        implementation(compose.dependencies.uiTooling)
                        implementation(compose.dependencies.preview)
                    }
                }
            }
            named("desktopMain") {
                dependencies {
                    if (composeConfig.ui)
                        implementation(compose.dependencies.desktop.common)
                }
            }
        }
    }
}

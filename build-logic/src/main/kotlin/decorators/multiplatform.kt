@file:Suppress("UnstableApiUsage")

package decorators

import MultiplatformDependencyHandler
import MultiplatformLibraryConfig
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencyResolveDetails
import org.gradle.api.checkIsRootProject
import org.gradle.api.libs
import org.gradle.api.version
import org.gradle.api.withExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.konan.target.Family

@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal fun Project.setMultiplatformLibrary(
    multiplatformLibraryConfig: MultiplatformLibraryConfig,
    multiplatformDependencyHandler: MultiplatformDependencyHandler,
) {
    val kmmExtension: KotlinMultiplatformExtension? = withExtension<KotlinMultiplatformExtension> {
        targetHierarchy.default {
            common {
                withJvm()
                withAndroid()
            }
            group("jsBased") {
                withJs()
            }
            group("posix") {
                withCompilations { it.target.let { target -> target is KotlinNativeTarget && target.konanTarget.family != Family.MINGW } }
            }
        }

        sourceSets.getByName("commonTest").dependencies {
            implementation("org.jetbrains.kotlin:kotlin-test")
            implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
        }

        targets.configureEach {
            compilations.configureEach {
                compilerOptions.configure {
                    //                allWarningsAsErrors.set(provider { KodeinLocalPropertiesPlugin.on(project).isNotTrue("allowWarnings") })
                }
            }
        }
    }

    afterEvaluate {
        kmmExtension?.sourceSets?.all {
            languageSettings.progressiveMode = true
        }
    }

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

internal fun Project.setComposeExperimental() {
    checkIsRootProject()
    allprojects {
        configurations.all {
            val conf = this
            resolutionStrategy.eachDependency {
                isDependencySuitableForVersionChange(conf) {
                    libs.version("jetbrainsCompose")?.let { useVersion(it) }
                }
            }
        }

    }
}

internal fun Project.setComposeMultiplatform() {
    extensions.configure<ComposeExtension> {
        kotlinCompilerPlugin.set(libs.version("jetbrainsComposeWasm"))
        kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=${libs.version("kotlin")}")
    }
}

private fun DependencyResolveDetails.isDependencySuitableForVersionChange(
    conf: Configuration,
    block: DependencyResolveDetails.() -> Unit,
) {
    with(requested.module.group) {
        when {
            startsWith("org.jetbrains.compose") && !startsWith("org.jetbrains.compose.compiler") -> {
                val isWasm = conf.name.contains("wasm", ignoreCase = true)
                val isJs = conf.name.contains("js", ignoreCase = true)
                if (!isWasm && !isJs) block()
            }
        }
    }
}

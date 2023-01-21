@file:Suppress("UnstableApiUsage")

package decorators

import config.AndroidConfig.AndroidLibraryConfig
import config.CompilationConfig
import config.ComposeConfig
import org.gradle.api.Project
import org.gradle.api.compose
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

internal fun Project.setMultiplatformLibrary(
    androidLibraryConfig: AndroidLibraryConfig,
    compilationConfig: CompilationConfig,
    composeConfig: ComposeConfig?,
    commonMainDependencies: KotlinDependencyHandler.() -> Unit,
    androidMainDependencies: KotlinDependencyHandler.() -> Unit,
    desktopMainDependencies: KotlinDependencyHandler.() -> Unit,
) {
    pluginManager.apply {
        apply("com.android.library")
        apply("kotlin-multiplatform")
    }
    extensions.configure<KotlinMultiplatformExtension> {
        android()
        jvm("desktop")

        sourceSets {
            named("commonMain") { dependencies(commonMainDependencies) }
            named("androidMain") { dependencies(androidMainDependencies) }
            named("desktopMain") { dependencies(desktopMainDependencies) }
            removeAll { androidLibraryConfig.ignoredSourceSets.contains(it.name) }
        }
    }

    composeConfig?.let(::setComposeMultiplatform)
    setKotlinCompilation(compilationConfig)
    setAndroidLibrary(androidLibraryConfig)
    setJUnit5()
}

@OptIn(ExperimentalComposeLibrary::class)
private fun Project.setComposeMultiplatform(composeConfig: ComposeConfig) {
    extensions.configure<KotlinMultiplatformExtension> {
        pluginManager.apply {
            apply("org.jetbrains.compose")
        }

        sourceSets {
            named("commonMain") {
                dependencies {
                    if (composeConfig.runtime)
                        compileOnly(compose.dependencies.runtime)
                    if (composeConfig.ui) {
                        compileOnly(compose.dependencies.foundation)
                        compileOnly(compose.dependencies.material3)
                    }
                }
            }
            named("desktopMain") {
                dependencies {
                    if (composeConfig.ui) compileOnly(compose.dependencies.desktop.common)
                }
            }
        }
    }
}

@file:Suppress("UnstableApiUsage")

package decorators

import config.AndroidConfig
import config.AndroidConfig.AndroidLibraryConfig
import config.CompilationConfig
import config.ComposeConfig
import org.gradle.api.Project
import org.gradle.api.compose
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

internal fun Project.setMultiplatformLibrary(
    androidLibraryConfig: AndroidLibraryConfig,
    androidCommonConfig: AndroidConfig.AndroidCommonConfig,
    compilationConfig: CompilationConfig,
    composeConfig: ComposeConfig?,
    commonMainDependencies: KotlinDependencyHandler.() -> Unit,
    androidMainDependencies: KotlinDependencyHandler.() -> Unit,
    desktopMainDependencies: KotlinDependencyHandler.() -> Unit,
) {
    extensions.configure<KotlinMultiplatformExtension> {
        android()
        jvm("desktop")

        sourceSets {
            named("commonMain") { dependencies(commonMainDependencies) }
            named("androidMain") { dependencies(androidMainDependencies) }
            named("desktopMain") { dependencies(desktopMainDependencies) }
            removeAll { androidLibraryConfig.ignoredSourceSets.contains(it.name) }

            all {
                languageSettings {
                    compilationConfig.featureOptIns.forEach {
                        optIn(it.flag)
                    }
                }
            }
        }
    }

    composeConfig?.let(::setComposeMultiplatform)
    setAndroidLibrary(androidLibraryConfig, androidCommonConfig, compilationConfig)
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
                        compileOnly(compose.dependencies.uiTooling)
                        compileOnly(compose.dependencies.preview)
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

@file:Suppress("UnstableApiUsage")

package decorators

import config.AndroidConfig
import config.CompilationConfig
import config.ComposeConfig
import org.gradle.api.Project
import org.gradle.api.compose
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

@OptIn(ExperimentalComposeLibrary::class)
internal fun Project.configureMultiplatformLibrary(
    androidConfig: AndroidConfig,
    compilationConfig: CompilationConfig,
    composeConfig: ComposeConfig? = null,
    commonMainDependencies: KotlinDependencyHandler.() -> Unit,
    androidMainDependencies: KotlinDependencyHandler.() -> Unit,
    desktopMainDependencies: KotlinDependencyHandler.() -> Unit,
) {
    extensions.configure<KotlinMultiplatformExtension> {
        pluginManager.apply {
            apply("com.android.library")
            apply("kotlin-multiplatform")
            if (composeConfig != null) {
                apply("org.jetbrains.compose")
            }
        }
        android()
        jvm("desktop")

        sourceSets {
            named("commonMain") {
                composeConfig?.run {
                    dependencies {
                        if (runtime)
                            compileOnly(compose.dependencies.runtime)
                        if (ui) {
                            compileOnly(compose.dependencies.foundation)
                            compileOnly(compose.dependencies.material3)
                        }
                    }
                }
                dependencies(commonMainDependencies)
            }
            named("androidMain") {
                dependencies(androidMainDependencies)
            }
            named("desktopMain") {
                composeConfig?.run {
                    dependencies {
                        if (ui) compileOnly(compose.dependencies.desktop.common)
                    }
                }

                dependencies(desktopMainDependencies)
            }

            removeAll { sourceSet ->
                setOf(
                    "androidAndroidTestRelease",
                    "androidTestFixtures",
                    "androidTestFixturesDebug",
                    "androidTestFixturesRelease",
                ).contains(sourceSet.name)
            }
        }
    }

    configureKotlinJvm(compilationConfig)
    setAndroidLibrary(androidConfig)
    configureJUnitTests()
}

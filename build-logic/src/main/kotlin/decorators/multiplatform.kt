@file:Suppress("UnstableApiUsage")

package decorators

import config.CompilationConfig
import org.gradle.api.Project
import org.gradle.api.compose
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@OptIn(ExperimentalComposeLibrary::class)
internal fun Project.configureMultiplatformLibrary(
    enableCompose: Boolean = false,
    compilationConfig: CompilationConfig,
    commonMainDependencies: KotlinDependencyHandler.() -> Unit,
    androidMainDependencies: KotlinDependencyHandler.() -> Unit,
    desktopMainDependencies: KotlinDependencyHandler.() -> Unit,
) {
    extensions.configure<KotlinMultiplatformExtension> {
        pluginManager.apply {
            apply("com.android.library")
            apply("kotlin-multiplatform")
            if (enableCompose) {
                apply("org.jetbrains.compose")
            }
        }
        android()
        jvm("desktop")

        sourceSets {
            named("commonMain") {
                if (enableCompose) {
                    dependencies {
                        compileOnly(compose.dependencies.runtime)
                        compileOnly(compose.dependencies.foundation)
                        compileOnly(compose.dependencies.material3)
                    }
                }

                dependencies(commonMainDependencies)
            }
            named("androidMain") {
                dependencies(androidMainDependencies)
            }
            named("desktopMain") {
                if (enableCompose) {
                    dependencies {
                        compileOnly(compose.dependencies.desktop.common)
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

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            allWarningsAsErrors = false
            jvmTarget = "11"
        }
    }

    configureAndroidLibrary(enableCompose, compilationConfig)
    configureJUnitTests()
}

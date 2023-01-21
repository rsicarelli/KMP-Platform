package decorators

import config.ComposeConfig
import org.gradle.api.Project
import org.gradle.api.compose
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@OptIn(ExperimentalComposeLibrary::class)
internal fun Project.setComposeMultiplatform(composeConfig: ComposeConfig) {
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

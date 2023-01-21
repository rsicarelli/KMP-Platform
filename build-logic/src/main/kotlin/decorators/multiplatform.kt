@file:Suppress("UnstableApiUsage")

package decorators

import config.AndroidConfig.AndroidLibraryConfig
import config.CompilationConfig
import config.ComposeConfig
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

internal fun Project.configureMultiplatformLibrary(
    androidLibraryConfig: AndroidLibraryConfig,
    compilationConfig: CompilationConfig,
    composeConfig: ComposeConfig? = null,
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
    configureKotlinJvm(compilationConfig)
    setAndroidLibrary(androidLibraryConfig)
    configureJUnitTests()
}

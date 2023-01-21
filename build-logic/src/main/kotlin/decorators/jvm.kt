package decorators

import com.android.build.gradle.BaseExtension
import config.CompilationConfig
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.withExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureJvmLibrary(
    compilationConfig: CompilationConfig = requireDefaults(),
) {
    with(pluginManager) {
        apply("java-library")
    }

    configureJavaCompatibility(compilationConfig)
    configureKotlinJvm(compilationConfig)
    configureJUnitTests()
}

internal fun Project.configureKotlinJvm(compilationConfig: CompilationConfig) {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            allWarningsAsErrors = compilationConfig.allWarningsAsErrors
            freeCompilerArgs = freeCompilerArgs + compilationConfig.extraFreeCompilerArgs
            jvmTarget = compilationConfig.jvmTarget
        }
    }
    withExtension<BaseExtension> {
        compileOptions {
            sourceCompatibility = compilationConfig.javaVersion
            targetCompatibility = compilationConfig.javaVersion
        }
    }
}

private fun Project.configureJavaCompatibility(compilationConfig: CompilationConfig) {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = compilationConfig.javaVersion
        targetCompatibility = compilationConfig.javaVersion
    }
}

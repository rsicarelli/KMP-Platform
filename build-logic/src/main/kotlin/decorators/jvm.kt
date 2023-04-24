package decorators

import config.CompilationConfig
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.setJvmLibrary(
    compilationConfig: CompilationConfig = requireDefaults(),
) {
    with(pluginManager) {
        apply("java-library")
    }

    setJavaCompatibility(compilationConfig)
    setKotlinCompilation(compilationConfig)
    setJUnit5()
}

internal fun Project.setKotlinCompilation(compilationConfig: CompilationConfig) {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            //            apiVersion = "1.8"
            //            languageVersion = "1.8"
        }
    }
}

private fun Project.setJavaCompatibility(compilationConfig: CompilationConfig) {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = compilationConfig.javaVersion
        targetCompatibility = compilationConfig.javaVersion
    }
}

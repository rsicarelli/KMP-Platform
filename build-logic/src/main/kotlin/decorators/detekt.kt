package decorators

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.checkIsRootProject
import org.gradle.api.libs
import org.gradle.api.version
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

internal fun Project.configureDetekt() {
    checkIsRootProject()

    plugins.apply("io.gitlab.arturbosch.detekt")

    extensions.configure<DetektExtension> {
        parallel = true
        toolVersion = libs.version("detekt")!!
        config = files("$projectDir/detekt.yml")
    }

    tasks.withType<Detekt> {
        setSource(files(projectDir))
        include("**/*.kt", "**/*.kts")
        exclude(".*/resources/.*", ".*/build/.*", "**/proto/**", "**/kspCaches/**", "**/ksp/**")
    }
}

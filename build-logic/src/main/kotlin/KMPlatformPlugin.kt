import decorators.PROJECT_DEFAULTS_KEY
import decorators.requireDefaults
import decorators.setAndroidApp
import decorators.setComponentPublication
import decorators.setDesktopApp
import decorators.setDetekt
import decorators.setMultiplatformLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

class KMPlatformPlugin : Plugin<Project> {

    override fun apply(target: Project) = Unit
}

fun Project.installDefaults(
    androidAppConfig: AndroidAppConfig? = null,
    desktopAppConfig: DesktopAppConfig? = null,
    multiplatformLibraryConfig: MultiplatformLibraryConfig,
    publicationConfig: PublicationConfig = PublicationConfig(
        group = group.toString(),
        remoteName = name,
    ),
) = extra.set(
    /* name = */ PROJECT_DEFAULTS_KEY,
    /* value = */ listOfNotNull(
        desktopAppConfig,
        multiplatformLibraryConfig,
        androidAppConfig,
        publicationConfig
    )
)

fun Project.installAndroidApp(
    androidAppConfig: AndroidAppConfig = requireDefaults(),
    compilationConfig: CompilationConfig = requireDefaults(),
) = setAndroidApp(
    appConfig = androidAppConfig,
    compilationConfig = compilationConfig
)

fun Project.installDesktopApp(
    desktopAppConfig: DesktopAppConfig = requireDefaults(),
    jvmDependencyHandler: KotlinDependencyHandler.() -> Unit = {},
) = setDesktopApp(desktopAppConfig, jvmDependencyHandler)

fun Project.installDetekt() = setDetekt()

fun Project.installComponentPublication(
    publicationTarget: PlatformPublicationTarget,
    artifactId: String = name,
    publicationConfig: PublicationConfig = requireDefaults(),
) = setComponentPublication(publicationTarget, artifactId, publicationConfig)

fun Project.installMultiplatformLibrary(
    multiplatformLibraryConfig: MultiplatformLibraryConfig = requireDefaults(),
    multiplatformDependencyHandler: MultiplatformDependencyHandler,
) = setMultiplatformLibrary(
    multiplatformLibraryConfig, multiplatformDependencyHandler
)

//fun Project.installComposeMultiplatform() = setComposeMultiplatform()
//
//fun Project.installComposeExperimental() = setComposeExperimental()

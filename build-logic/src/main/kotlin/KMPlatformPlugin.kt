import config.AndroidConfig.AndroidAppConfig
import config.AndroidConfig.AndroidCommonConfig
import config.AndroidConfig.AndroidLibraryConfig
import config.CompilationConfig
import config.ComposeConfig
import config.DesktopAppConfig
import config.PlatformPublicationTarget
import config.PublicationConfig
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
    androidCommonConfig: AndroidCommonConfig = AndroidCommonConfig(),
    androidLibraryConfig: AndroidLibraryConfig = AndroidLibraryConfig(),
    compilationConfig: CompilationConfig = CompilationConfig(),
    publicationConfig: PublicationConfig = PublicationConfig(
        group = group.toString(),
        remoteName = name,
    ),
) = extra.set(
    /* name = */ PROJECT_DEFAULTS_KEY,
    /* value = */ listOfNotNull(
        desktopAppConfig,
        androidCommonConfig,
        androidLibraryConfig,
        androidAppConfig,
        compilationConfig,
        publicationConfig
    )
)

fun Project.installAndroidApp(
    androidAppConfig: AndroidAppConfig = requireDefaults(),
    androidCommonConfig: AndroidCommonConfig = requireDefaults(),
    compilationConfig: CompilationConfig = requireDefaults(),
) = setAndroidApp(
    appConfig = androidAppConfig,
    commonConfig = androidCommonConfig,
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
) = setComponentPublication(publicationTarget, artifactId)

fun Project.installMultiplatformLibrary(
    compilationConfig: CompilationConfig = requireDefaults(),
    androidLibraryConfig: AndroidLibraryConfig = requireDefaults(),
    androidCommonConfig: AndroidCommonConfig = requireDefaults(),
    composeConfig: ComposeConfig? = null,
    commonMainDependencies: KotlinDependencyHandler.() -> Unit = { },
    androidMainDependencies: KotlinDependencyHandler.() -> Unit = { },
    desktopMainDependencies: KotlinDependencyHandler.() -> Unit = { },
) = setMultiplatformLibrary(
    compilationConfig = compilationConfig,
    androidLibraryConfig = androidLibraryConfig,
    androidCommonConfig = androidCommonConfig,
    composeConfig = composeConfig,
    commonMainDependencies = commonMainDependencies,
    androidMainDependencies = androidMainDependencies,
    desktopMainDependencies = desktopMainDependencies,
)

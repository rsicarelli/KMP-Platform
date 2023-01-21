import config.AndroidConfig.AndroidAppConfig
import config.AndroidConfig.AndroidCommonConfig
import config.AndroidConfig.AndroidLibraryConfig
import config.CompilationConfig
import config.ComposeConfig
import config.DesktopAppConfig
import config.PublicationConfig
import decorators.PROJECT_DEFAULTS_KEY
import decorators.configureJvmLibraryPublication
import decorators.requireDefaults
import decorators.setAndroidApp
import decorators.setAndroidLibraryPublication
import decorators.setDesktopApp
import decorators.setDetekt
import decorators.setJvmLibrary
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
) = setAndroidApp(
    appConfig = androidAppConfig,
)

fun Project.installDesktopApp(
    desktopAppConfig: DesktopAppConfig,
) = setDesktopApp(desktopAppConfig)

fun Project.installDetekt() = setDetekt()

fun Project.installJvmLibrary(
    compilationConfig: CompilationConfig = requireDefaults(),
) = setJvmLibrary(compilationConfig)

fun Project.installJvmLibraryPublication(
    version: String,
    artefactId: String,
) = configureJvmLibraryPublication(version = version, artefactId = artefactId)

fun Project.installAndroidLibraryPublication(
    version: String,
    artefactId: String,
) = setAndroidLibraryPublication(version = version, artefactId = artefactId)

fun Project.installMultiplatformLibrary(
    compilationConfig: CompilationConfig = requireDefaults(),
    androidLibraryConfig: AndroidLibraryConfig = requireDefaults(),
    composeConfig: ComposeConfig? = null,
    commonMainDependencies: KotlinDependencyHandler.() -> Unit = { },
    androidMainDependencies: KotlinDependencyHandler.() -> Unit = { },
    desktopMainDependencies: KotlinDependencyHandler.() -> Unit = { },
) = setMultiplatformLibrary(
    compilationConfig = compilationConfig,
    androidLibraryConfig = androidLibraryConfig,
    composeConfig = composeConfig,
    commonMainDependencies = commonMainDependencies,
    androidMainDependencies = androidMainDependencies,
    desktopMainDependencies = desktopMainDependencies,
)

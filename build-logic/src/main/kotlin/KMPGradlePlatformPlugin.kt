import config.AndroidConfig.AndroidAppConfig
import config.AndroidConfig.AndroidCommonConfig
import config.AndroidConfig.AndroidLibraryConfig
import config.CompilationConfig
import config.ComposeConfig
import config.DesktopAppConfig
import config.PublicationConfig
import decorators.PROJECT_DEFAULTS_KEY
import decorators.configureAndroidLibraryPublication
import decorators.configureDetekt
import decorators.configureJvmLibrary
import decorators.configureJvmLibraryPublication
import decorators.configureMultiplatformLibrary
import decorators.requireDefaults
import decorators.setAndroidApp
import decorators.setupDesktopApp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

class KMPGradlePlatformPlugin : Plugin<Project> {

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
) = setupDesktopApp(desktopAppConfig)

fun Project.installDetekt() = configureDetekt()

fun Project.installJvmLibrary(
    compilationConfig: CompilationConfig = requireDefaults(),
) = configureJvmLibrary(compilationConfig)

fun Project.installJvmLibraryPublication(
    version: String,
    artefactId: String,
) = configureJvmLibraryPublication(version = version, artefactId = artefactId)

fun Project.installAndroidLibraryPublication(
    version: String,
    artefactId: String,
) = configureAndroidLibraryPublication(version = version, artefactId = artefactId)

fun Project.installMultiplatformLibrary(
    compilationConfig: CompilationConfig = requireDefaults(),
    androidLibraryConfig: AndroidLibraryConfig = requireDefaults(),
    composeConfig: ComposeConfig = requireDefaults(),
    commonMainDependencies: KotlinDependencyHandler.() -> Unit = { },
    androidMainDependencies: KotlinDependencyHandler.() -> Unit = { },
    desktopMainDependencies: KotlinDependencyHandler.() -> Unit = { },
) = configureMultiplatformLibrary(
    compilationConfig = compilationConfig,
    androidLibraryConfig = androidLibraryConfig,
    composeConfig = composeConfig,
    commonMainDependencies = commonMainDependencies,
    androidMainDependencies = androidMainDependencies,
    desktopMainDependencies = desktopMainDependencies,
)

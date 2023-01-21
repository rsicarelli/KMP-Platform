import config.AndroidConfig
import config.CompilationConfig
import config.ComposeConfig
import config.DesktopAppConfig
import config.PublicationConfig
import decorators.PROJECT_DEFAULTS_KEY
import decorators.setAndroidApp
import decorators.configureAndroidLibraryPublication
import decorators.configureDetekt
import decorators.configureJvmLibrary
import decorators.configureJvmLibraryPublication
import decorators.configureMultiplatformLibrary
import decorators.requireDefaults
import decorators.setupDesktopApp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

class KMPGradlePlatformPlugin : Plugin<Project> {

    override fun apply(target: Project) = Unit
}

fun Project.installDefaults(
    androidConfig: AndroidConfig = AndroidConfig(),
    compilationConfig: CompilationConfig = CompilationConfig(),
    publicationConfig: PublicationConfig = PublicationConfig(
        group = group.toString(),
        remoteName = name,
    ),
) = extra.set(
    /* name = */ PROJECT_DEFAULTS_KEY,
    /* value = */ listOfNotNull(
        androidConfig,
        compilationConfig,
        publicationConfig
    )
)

fun Project.installAndroidApp(
    applicationId: String,
    versionCode: Int,
    versionName: String,
    androidConfig: AndroidConfig = requireDefaults(),
) = setAndroidApp(
    applicationId = applicationId,
    versionName = versionName,
    versionCode = versionCode,
    androidConfig = androidConfig,
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
    androidConfig: AndroidConfig = requireDefaults(),
    composeConfig: ComposeConfig = requireDefaults(),
    commonMainDependencies: KotlinDependencyHandler.() -> Unit = { },
    androidMainDependencies: KotlinDependencyHandler.() -> Unit = { },
    desktopMainDependencies: KotlinDependencyHandler.() -> Unit = { },
) = configureMultiplatformLibrary(
    compilationConfig = compilationConfig,
    androidConfig = androidConfig,
    composeConfig = composeConfig,
    commonMainDependencies = commonMainDependencies,
    androidMainDependencies = androidMainDependencies,
    desktopMainDependencies = desktopMainDependencies,
)

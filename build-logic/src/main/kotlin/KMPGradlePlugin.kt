import config.AndroidConfig
import config.CompilationConfig
import config.DesktopConfig
import config.PublicationConfig
import decorators.PROJECT_DEFAULTS_KEY
import decorators.configureAndroidApp
import decorators.configureAndroidLibrary
import decorators.configureAndroidLibraryPublication
import decorators.configureDetekt
import decorators.configureJvmLibrary
import decorators.configureJvmLibraryPublication
import decorators.configureMultiplatformLibrary
import decorators.requireDefaults
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

class KMPGradlePlugin : Plugin<Project> {

    override fun apply(target: Project) = Unit
}

fun Project.installDefaults(
    androidConfig: AndroidConfig = AndroidConfig(),
    desktopConfig: DesktopConfig,
    compilationConfig: CompilationConfig = CompilationConfig(),
    publicationConfig: PublicationConfig = PublicationConfig(
        group = group.toString(),
        remoteName = name,
    ),
) = extra.set(
    /* name = */ PROJECT_DEFAULTS_KEY,
    /* value = */ listOfNotNull(
        androidConfig,
        desktopConfig,
        compilationConfig,
        publicationConfig
    )
)

fun Project.installAndroidLibrary(
    enableCompose: Boolean = false,
    compilationConfig: CompilationConfig = requireDefaults(),
) = configureAndroidLibrary(
    enableCompose = enableCompose,
    compilationConfig = compilationConfig,
)

fun Project.installAndroidApp(
    applicationId: String,
    versionCode: Int,
    versionName: String,
    compilationConfig: CompilationConfig = requireDefaults(),
    enableCompose: Boolean,
) = configureAndroidApp(
    applicationId = applicationId,
    versionCode = versionCode,
    versionName = versionName,
    compilationConfig = compilationConfig,
    enableCompose = enableCompose,
)

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
    enableCompose: Boolean = false,
    compilationConfig: CompilationConfig = requireDefaults(),
    commonMainDependencies: KotlinDependencyHandler.() -> Unit = { },
    androidMainDependencies: KotlinDependencyHandler.() -> Unit = { },
    desktopMainDependencies: KotlinDependencyHandler.() -> Unit = { },
) = configureMultiplatformLibrary(
    enableCompose = enableCompose,
    compilationConfig = compilationConfig,
    commonMainDependencies = commonMainDependencies,
    androidMainDependencies = androidMainDependencies,
    desktopMainDependencies = desktopMainDependencies
)

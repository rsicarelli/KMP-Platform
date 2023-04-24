import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

data class MultiplatformLibraryConfig(
    val androidLibraryConfig: AndroidLibraryConfig = AndroidLibraryConfig(),
    val iOSConfig: iOSLibraryConfig = iOSLibraryConfig(),
    val compilationConfig: CompilationConfig = CompilationConfig(),
    val composeConfig: ComposeConfig? = null,
)

typealias CommonDependencies = KotlinDependencyHandler.() -> Unit
typealias AndroidDependencies = KotlinDependencyHandler.() -> Unit
typealias DesktopDependencies = KotlinDependencyHandler.() -> Unit
typealias IOSDependencies = KotlinDependencyHandler.() -> Unit

class MultiplatformDependencyHandler(
    val common: CommonDependencies = { },
    val android: AndroidDependencies = { },
    val desktop: DesktopDependencies = { },
    val iOS: IOSDependencies = { },
)

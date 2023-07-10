plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("jvm") apply false
    kotlin("multiplatform") apply false
    kotlin("android") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
    id("io.gitlab.arturbosch.detekt") apply false
    alias(libs.plugins.rsicarelli.kmplatform) apply false
}

group = "com.rsicarelli.kmplatform"
version = libs.versions.kmplatform.get()

installDefaults(
    multiplatformLibraryConfig = MultiplatformLibraryConfig(
        androidLibraryConfig = AndroidLibraryConfig(),
        iOSConfig = iOSLibraryConfig(),
        compilationConfig = CompilationConfig(),
    ),
)
installDetekt()
installComposeExperimental()

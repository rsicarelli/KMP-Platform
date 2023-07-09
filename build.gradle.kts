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

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        mavenLocal()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven("https://jitpack.io")
    }
    configurations.all {
        val conf = this
        conf.resolutionStrategy.eachDependency {
            val isWasm = conf.name.contains("wasm", true)
            val isJs = conf.name.contains("js", true)
            val isComposeGroup = requested.module.group.startsWith("org.jetbrains.compose")
            val isComposeCompiler = requested.module.group.startsWith("org.jetbrains.compose.compiler")
            if (isComposeGroup && !isComposeCompiler && !isWasm && !isJs) {
                useVersion(libs.versions.jetbrainsCompose.get())
            }
            if (requested.module.name.startsWith("kotlin-stdlib")) {
                useVersion(libs.versions.kotlin.get())
            }
        }
    }
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

@file:Suppress("UnstableApiUsage")

package decorators

import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryBuildType
import com.android.build.gradle.LibraryExtension
import config.AndroidConfig
import config.AndroidConfig.AndroidAppConfig
import config.AndroidConfig.AndroidBuildType
import config.AndroidConfig.AndroidCommonConfig
import config.AndroidConfig.AndroidLibraryConfig
import config.AndroidConfig.DebugBuildType
import config.CompilationConfig
import org.gradle.api.Project
import org.gradle.api.androidCommonExtension
import org.gradle.api.projectNamespace
import org.gradle.api.withExtension

typealias AndroidAppExtension = ApplicationExtension
typealias AndroidLibraryExtension = LibraryExtension

private fun Project.setAndroidCommon(
    commonConfig: AndroidCommonConfig,
    compilationConfig: CompilationConfig,
) = androidCommonExtension {
    namespace = projectNamespace
    compileSdk = commonConfig.compileSdkVersion

    defaultConfig {
        minSdk = commonConfig.minSdkVersion
    }

    compileOptions {
        sourceCompatibility = compilationConfig.javaVersion
        targetCompatibility = compilationConfig.javaVersion
    }

    if (commonConfig.packagingExcludes.isNotEmpty()) {
        packagingOptions {
            resources.excludes.addAll(commonConfig.packagingExcludes)
        }
    }
    lint {
        abortOnError = commonConfig.lintOptions.abortOnError
    }
}

internal fun Project.setAndroidApp(
    appConfig: AndroidAppConfig,
    commonConfig: AndroidCommonConfig,
    compilationConfig: CompilationConfig,
) = run {
    setAndroidCommon(commonConfig, compilationConfig)
    withExtension<AndroidAppExtension> {
        defaultConfig {
            targetSdk = commonConfig.targetSdkVersion
            applicationId = appConfig.id
            versionCode = appConfig.version.code
            versionName = appConfig.version.formattedName
        }

        buildTypes {
            commonConfig.buildTypes.forEach { androidBuildType ->
                when (androidBuildType) {
                    DebugBuildType -> debug { applyFrom(androidBuildType) }
                    AndroidConfig.ReleaseBuildType -> release { applyFrom(androidBuildType) }
                    else -> getByName(androidBuildType.name) { applyFrom(androidBuildType) }
                }
            }
        }
    }
}

internal fun Project.setAndroidLibrary(
    libraryConfig: AndroidLibraryConfig,
    commonConfig: AndroidCommonConfig,
    compilationConfig: CompilationConfig,
) = run {
    setAndroidCommon(commonConfig, compilationConfig)
    withExtension<AndroidLibraryExtension> {
        compileSdk = commonConfig.compileSdkVersion
        defaultConfig {
            aarMetadata.minCompileSdk = commonConfig.minSdkVersion
            libraryConfig.consumerProguardFiles.forEach {
                consumerProguardFile(it)
            }
        }
        sourceSets {
            getByName("main") {
                manifest.srcFile(libraryConfig.manifestPath)
            }
        }
        buildTypes {
            commonConfig.buildTypes.forEach { androidBuildType ->
                when (androidBuildType) {
                    DebugBuildType -> debug { applyFrom(androidBuildType) }
                    AndroidConfig.ReleaseBuildType -> release { applyFrom(androidBuildType) }
                    else -> getByName(androidBuildType.name) { applyFrom(androidBuildType) }
                }
            }
        }
        buildFeatures {
            androidResources = libraryConfig.buildFeaturesConfig.generateAndroidResources
            resValues = libraryConfig.buildFeaturesConfig.generateResValues
            buildConfig = libraryConfig.buildFeaturesConfig.generateBuildConfig
        }
        lint { abortOnError = libraryConfig.lintOptions.abortOnError }
    }
}

private fun ApplicationBuildType.applyFrom(androidBuildType: AndroidBuildType) {
    isDebuggable = androidBuildType.isDebuggable
    isMinifyEnabled = androidBuildType.isMinifyEnabled
    isShrinkResources = androidBuildType.shrinkResources
    multiDexEnabled = androidBuildType.multidex
    versionNameSuffix = androidBuildType.versionNameSuffix
}

private fun LibraryBuildType.applyFrom(androidBuildType: AndroidBuildType) {
    isMinifyEnabled = androidBuildType.isMinifyEnabled
    multiDexEnabled = androidBuildType.multidex
}

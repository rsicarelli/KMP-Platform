@file:Suppress("UnstableApiUsage")

package decorators

import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryBuildType
import com.android.build.gradle.LibraryExtension
import AndroidAppConfig
import AndroidBuildType
import AndroidConfig
import AndroidLibraryConfig
import CompilationConfig
import DebugBuildType
import ReleaseBuildType
import org.gradle.api.Project
import org.gradle.api.androidCommonExtension
import org.gradle.api.projectNamespace
import org.gradle.api.withExtension

typealias AndroidAppExtension = ApplicationExtension
typealias AndroidLibraryExtension = LibraryExtension

private fun Project.setAndroidCommon(
    commonConfig: AndroidConfig,
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
    lint.abortOnError = commonConfig.lintOptions.abortOnError
}

internal fun Project.setAndroidApp(
    appConfig: AndroidAppConfig,
    compilationConfig: CompilationConfig,
) = run {
    setAndroidCommon(appConfig, compilationConfig)
    withExtension<AndroidAppExtension> {
        defaultConfig {
            targetSdk = appConfig.targetSdkVersion
            applicationId = appConfig.id
            versionCode = appConfig.version.code
            versionName = appConfig.version.formattedName
        }

        buildTypes {
            appConfig.buildTypes.forEach { androidBuildType ->
                when (androidBuildType) {
                    DebugBuildType -> debug { applyFrom(androidBuildType) }
                    ReleaseBuildType -> release { applyFrom(androidBuildType) }
                    else -> getByName(androidBuildType.name) { applyFrom(androidBuildType) }
                }
            }
        }
    }
}

internal fun Project.setAndroidLibrary(
    libraryConfig: AndroidLibraryConfig,
    compilationConfig: CompilationConfig,
) = run {
    setAndroidCommon(libraryConfig, compilationConfig)
    withExtension<AndroidLibraryExtension> {
        compileSdk = libraryConfig.compileSdkVersion
        defaultConfig {
            aarMetadata.minCompileSdk = libraryConfig.minSdkVersion
            libraryConfig.consumerProguardFiles.forEach {
                consumerProguardFile(it)
            }
        }
        sourceSets {
            getByName(libraryConfig.sourceSetName) {
                manifest.srcFile(libraryConfig.manifestPath)
//                res.srcDirs(libraryConfig.resPath)
//                resources.srcDirs(libraryConfig.resourcesPath)
            }
        }
        buildTypes {
            libraryConfig.buildTypes.forEach { androidBuildType ->
                when (androidBuildType) {
                    DebugBuildType -> debug { applyFrom(androidBuildType) }
                    ReleaseBuildType -> release { applyFrom(androidBuildType) }
                    else -> getByName(androidBuildType.name) { applyFrom(androidBuildType) }
                }
            }
        }
        buildFeatures {
            androidResources = libraryConfig.buildFeaturesConfig.generateAndroidResources
            resValues = libraryConfig.buildFeaturesConfig.generateResValues
//            buildConfig = libraryConfig.buildFeaturesConfig.generateBuildConfig
        }
        libraryVariants.all {
            generateBuildConfigProvider.get().enabled = libraryConfig.buildFeaturesConfig.generateBuildConfig

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

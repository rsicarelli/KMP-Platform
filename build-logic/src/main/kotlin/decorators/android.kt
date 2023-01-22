@file:Suppress("UnstableApiUsage")

package decorators

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import config.AndroidConfig
import config.AndroidConfig.AndroidAppConfig
import config.AndroidConfig.AndroidCommonConfig
import config.AndroidConfig.AndroidLibraryConfig
import org.gradle.api.Project
import org.gradle.api.projectNamespace
import org.gradle.api.withExtension

typealias AndroidLibraryExtension = LibraryExtension
typealias AndroidBaseExtension = BaseExtension
typealias AndroidAppExtension = BaseAppModuleExtension

internal fun Project.setAndroidApp(appConfig: AndroidAppConfig) = run {
    setAndroidCommon()
    withExtension<AndroidAppExtension> {
        appConfig.run {
            defaultConfig {
                applicationId = id
                versionCode = version.code
                versionName = version.formattedName
            }
            setLint(lintOptions)
        }
    }
}

internal fun Project.setAndroidLibrary(libraryConfig: AndroidLibraryConfig) = run {
    setAndroidCommon()
    withExtension<AndroidLibraryExtension> {
        libraryConfig.run {
            setManifestPath(manifestPath = manifestPath)
            setBuildFeatures(buildFeaturesConfig = buildFeaturesConfig)
            setLint(abortOnError = libraryConfig.lintOptions)
            setLibraryVariants(
                proguardFiles = libraryConfig.consumerProguardFiles,
                generateBuildConfig = libraryConfig.buildFeaturesConfig.generateBuildConfig
            )
        }
    }
}

private fun Project.setAndroidCommon(
    commonConfig: AndroidCommonConfig = requireDefaults(),
) = withExtension<AndroidBaseExtension> {
    namespace = projectNamespace

    commonConfig.run {
        compileSdkVersion(compileSdkVersion)
        defaultConfig.minSdk = minSdkVersion
        defaultConfig.targetSdk = targetSdkVersion
        defaultConfig.aarMetadata {
            minCompileSdk = minSdkVersion
        }

        packagingOptions {
            resources.excludes.addAll(commonConfig.packagingExcludes)
        }
    }
}

private fun CommonExtension<*, *, *, *>.setLint(abortOnError: AndroidConfig.LintOptions) {
    lint { this.abortOnError = abortOnError.abortOnError }
}

private fun AndroidLibraryExtension.setBuildFeatures(buildFeaturesConfig: AndroidLibraryConfig.AndroidBuildFeaturesConfig) =
    buildFeatures {
        androidResources = buildFeaturesConfig.generateAndroidResources
        resValues = buildFeaturesConfig.generateResValues
    }

private fun AndroidLibraryExtension.setManifestPath(manifestPath: String) =
    sourceSets {
        named("main") {
            manifest.srcFile(manifestPath)
        }
    }

private fun AndroidLibraryExtension.setLibraryVariants(
    proguardFiles: Sequence<String>,
    generateBuildConfig: Boolean,
) = libraryVariants.all {
    buildTypes {
        defaultConfig {
            consumerProguardFiles(proguardFiles.first())
        }
    }
    generateBuildConfigProvider.get().enabled = generateBuildConfig
}


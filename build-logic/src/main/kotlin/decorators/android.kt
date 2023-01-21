@file:Suppress("UnstableApiUsage")

package decorators

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import config.AndroidConfig
import org.gradle.api.Project
import org.gradle.api.projectNamespace
import org.gradle.api.withExtension

private fun Project.setAndroidCommon(
    androidConfig: AndroidConfig,
) = withExtension<BaseExtension> {
    namespace = projectNamespace

    compileSdkVersion(androidConfig.compileSdkVersion)

    defaultConfig {
        minSdk = androidConfig.minSdkVersion
        targetSdk = androidConfig.targetSdkVersion
    }

    packagingOptions {
        resources.excludes.addAll(androidConfig.packagingExcludes)
    }
}

internal fun Project.setAndroidApp(
    applicationId: String,
    versionName: String,
    versionCode: Int,
    androidConfig: AndroidConfig,
) {
    setAndroidCommon(androidConfig)

    withExtension<BaseAppModuleExtension> {
        defaultConfig {
            this.applicationId = applicationId
            this.versionCode = versionCode
            this.versionName = "$versionName.$versionCode"
        }

        setLint(androidConfig.lintAbortOnError)
    }
}

internal fun Project.setAndroidLibrary(
    androidConfig: AndroidConfig,
) {
    setAndroidCommon(androidConfig)

    withExtension<LibraryExtension> {
        androidConfig.run {
            setManifestPath(manifestPath = manifestPath)
            setBuildFeatures(buildFeaturesConfig = buildFeaturesConfig)
            setLint(abortOnError = lintAbortOnError)
            setLibraryVariants(
                proguardFiles = androidConfig.consumerProguardFiles,
                generateBuildConfig = androidConfig.buildFeaturesConfig.generateBuildConfig
            )
        }
    }
}

private fun CommonExtension<*, *, *, *>.setLint(abortOnError: Boolean) {
    lint { this.abortOnError = abortOnError }
}

private fun LibraryExtension.setBuildFeatures(buildFeaturesConfig: AndroidConfig.AndroidBuildFeaturesConfig) =
    buildFeatures {
        androidResources = buildFeaturesConfig.generateAndroidResources
        resValues = buildFeaturesConfig.generateResValues
    }

private fun LibraryExtension.setManifestPath(manifestPath: String) =
    sourceSets {
        named("main") {
            manifest.srcFile(manifestPath)
        }
    }

private fun LibraryExtension.setLibraryVariants(
    proguardFiles: Sequence<String>,
    generateBuildConfig: Boolean,
) = libraryVariants.all {
    buildTypes {
        defaultConfig {
            consumerProguardFiles(proguardFiles)
        }
    }
    generateBuildConfigProvider.get().enabled = generateBuildConfig
}


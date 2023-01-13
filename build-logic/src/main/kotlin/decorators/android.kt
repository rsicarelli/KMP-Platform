@file:Suppress("UnstableApiUsage")

package decorators

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withGroovyBuilder

internal fun Project.configureAndroidLibrary() {
    configureAndroidCommon()

    extensions.configure<LibraryExtension> {
        libraryVariants.all {
            buildTypes {
                defaultConfig {
                    consumerProguardFiles("proguard-rules.pro", "consumer-rules.pro")
                }
            }
            generateBuildConfigProvider.get().enabled = false
        }

        configureLint()

        sourceSets {
            named("main") {
                manifest.srcFile("src/androidMain/AndroidManifest.xml")
            }
        }
    }
}

fun Project.configureAndroidApp(
    applicationId: String,
    versionCode: Int,
    versionName: String,
) {
    configureAndroidCommon()

    extensions.configure<BaseAppModuleExtension> {
        defaultConfig {
            this.applicationId = applicationId
            this.versionCode = versionCode
            this.versionName = versionName
        }

        configureLint()
    }
}

private fun Project.configureAndroidCommon(
    compileSdk: Int = 33,
    minSdk: Int = 26,
    targetSdk: Int = 33,
) {
    extensions.configure<BaseExtension> {
        namespace = "app.dreamlightpal.${project.name}"

        compileSdkVersion(compileSdk)

        defaultConfig {
            this.minSdk = minSdk
            this.targetSdk = targetSdk
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        withGroovyBuilder {
            "kotlinOptions" {
                setProperty("jvmTarget", "11")
            }
        }
    }
}

private fun CommonExtension<*, *, *, *>.configureLint() {
    lint { abortOnError = false }
}

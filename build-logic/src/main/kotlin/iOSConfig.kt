@file:Suppress("ClassName")

import org.jetbrains.kotlin.gradle.plugin.mpp.Framework

sealed interface iOSConfig

data class iOSLibraryConfig(
    val cocoapodsConfig: CocoapodsConfig? = null,
    val targets: List<iOSTargets> = iOSTargets.values().toList(),
    val framework: Framework.() -> Unit = {},
    val frameworkName: String = "KMP-Platform",
    val isStatic: Boolean = true,
) : iOSConfig

enum class iOSTargets {

    X64, Arm64, SimulatorArm64
}

data class CocoapodsConfig(
    val version: String,
    val summary: String,
    val homepage: String,
    val deploymentTarget: String = "14.1",
    val podfilePath: String,
    val frameworkConfig: FrameworkConfig,
    val extraSpecAttributes: List<String>,
)

data class FrameworkConfig(
    val baseName: String,
    val isStatic: Boolean,
)

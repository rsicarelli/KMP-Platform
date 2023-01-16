package decorators

import config.DesktopConfig
import org.gradle.api.Project
import org.gradle.api.compose
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

internal fun Project.setupDesktopApp(
    desktopConfig: DesktopConfig = requireDefaults(),
    jvmDependencyHandler: KotlinDependencyHandler.() -> Unit = {},
) {
    extensions.configure<KotlinMultiplatformExtension> {
        jvm {
            withJava()
        }
        sourceSets {
            named("jvmMain") {
                dependencies(jvmDependencyHandler)
            }
        }
    }

    compose.configure<DesktopExtension> {
        application {
            mainClass = desktopConfig.mainClass
            nativeDistributions {
                packageName = desktopConfig.packageName
                packageVersion = desktopConfig.packageVersion
                description = desktopConfig.description
                copyright = desktopConfig.copyright
                vendor = desktopConfig.vendor

                targetFormats(*desktopConfig.targetFormats.toTypedArray())

                val iconsRoot = project.file(desktopConfig.resourceRootPath)

                linux {
                    iconFile.set(iconsRoot.resolve(desktopConfig.linuxConfig.iconPath))
                }

                windows {
                    iconFile.set(iconsRoot.resolve(desktopConfig.windowsConfig.iconPath))
                    upgradeUuid = desktopConfig.windowsConfig.upgradeUuid
                    menuGroup = desktopConfig.windowsConfig.menuGroup
                    perUserInstall = desktopConfig.windowsConfig.perUserInstall
                }

                macOS {
                    bundleID = desktopConfig.macOSConfig.bundleID
                    iconFile.set(iconsRoot.resolve(desktopConfig.macOSConfig.iconPath))
                }
            }
        }
    }
}

package decorators

import DesktopAppConfig
import org.gradle.api.Project
import org.gradle.api.compose
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

internal fun Project.setDesktopApp(
    desktopAppConfig: DesktopAppConfig,
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
            mainClass = desktopAppConfig.mainClass
            nativeDistributions {
                packageName = desktopAppConfig.packageName
                packageVersion = desktopAppConfig.packageVersion
                description = desktopAppConfig.description
                copyright = desktopAppConfig.copyright
                vendor = desktopAppConfig.vendor

                targetFormats(*desktopAppConfig.targetFormats.toTypedArray())

                val iconsRoot = project.file(desktopAppConfig.resourceRootPath)

                linux {
                    iconFile.set(iconsRoot.resolve(desktopAppConfig.linuxConfig.iconPath))
                }

                windows {
                    iconFile.set(iconsRoot.resolve(desktopAppConfig.windowsConfig.iconPath))
                    upgradeUuid = desktopAppConfig.windowsConfig.upgradeUuid
                    menuGroup = desktopAppConfig.windowsConfig.menuGroup
                    perUserInstall = desktopAppConfig.windowsConfig.perUserInstall
                }

                macOS {
                    bundleID = desktopAppConfig.macOSConfig.bundleID
                    iconFile.set(iconsRoot.resolve(desktopAppConfig.macOSConfig.iconPath))
                }
            }
        }
    }
}

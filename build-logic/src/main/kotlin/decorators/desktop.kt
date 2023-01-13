package decorators

import org.gradle.api.Project
import org.gradle.api.compose
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

fun Project.setupDesktopApp(
    dependencyHandler: KotlinDependencyHandler.() -> Unit = {},
) {
    extensions.configure<KotlinMultiplatformExtension> {
        jvm {
            withJava()
        }
        sourceSets {
            named("jvmMain") {
                dependencies(dependencyHandler)
            }
        }
    }

    compose.configure<DesktopExtension> {
        application {
            mainClass = "app.dreamlightpal.MainKt"
            nativeDistributions {
                packageName = "DreamlightPal"
                packageVersion = "1.0.0"
                description = "todo"
                copyright = "© 2022 rsicarelli. All rights reserved."
                vendor = "rsicarelli"

                targetFormats(
                    TargetFormat.Dmg,
                    TargetFormat.Msi,
                    TargetFormat.Deb
                )

                val iconsRoot = project.file("src/main/resources/drawables")

                linux {
                    //                    iconFile.set(iconsRoot.resolve("launcher_icons/linux.png"))
                }

                windows {
                    //                    iconFile.set(iconsRoot.resolve("launcher_icons/windows.ico"))
                    // Wondering what the heck is this? See : https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
                    upgradeUuid = "//todo"
                    menuGroup = packageName
                    perUserInstall = true
                }

                macOS {
                    bundleID = "app.dreamlightpal"
                    //                    iconFile.set(iconsRoot.resolve("launcher_icons/macos.icns"))
                }
            }
        }

    }
}

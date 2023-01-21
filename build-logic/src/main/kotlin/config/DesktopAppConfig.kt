package config

import org.jetbrains.compose.desktop.application.dsl.TargetFormat

data class DesktopAppConfig(
    val mainClass: String,
    val packageName: String,
    val packageVersion: String,
    val description: String,
    val copyright: String,
    val vendor: String,
    val resourceRootPath: String,
    val targetFormats: List<TargetFormat>,
    val windowsConfig: WindowsConfig,
    val macOSConfig: MacOSConfig,
    val linuxConfig: LinuxConfig,
) {

    data class LinuxConfig(
        val iconPath: String,
    )

    data class WindowsConfig(
        // https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
        val iconPath: String,
        val upgradeUuid: String,
        val menuGroup: String,
        val perUserInstall: Boolean,
    )

    data class MacOSConfig(
        val iconPath: String,
        val bundleID: String,
    )
}

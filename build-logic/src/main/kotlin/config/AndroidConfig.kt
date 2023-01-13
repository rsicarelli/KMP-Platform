package config

data class AndroidConfig(
    val minSdkVersion: Int = 24,
    val compileSdkVersion: Int = 33,
    val targetSdkVersion: Int = 33,
    val enableCoreLibraryDesugaring: Boolean = true,
    val packagingExcludes: List<String> = emptyList(),
    val lintAbortOnError: Boolean = false,
)

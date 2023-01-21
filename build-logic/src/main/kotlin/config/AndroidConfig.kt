package config

data class AndroidConfig(
    val minSdkVersion: Int = 24,
    val compileSdkVersion: Int = 33,
    val targetSdkVersion: Int = 33,
    val packagingExcludes: List<String> = emptyList(),
    val lintAbortOnError: Boolean = false,
    val consumerProguardFiles: Sequence<String> = sequenceOf(
        "proguard-rules.pro",
        "consumer-rules.pro",
        "proguard.txt"
    ),
    val manifestPath: String = "src/androidMain/AndroidManifest.xml",
    val buildFeaturesConfig: AndroidBuildFeaturesConfig = AndroidBuildFeaturesConfig(),
) {

    data class AndroidBuildFeaturesConfig(
        val generateAndroidResources: Boolean = false,
        val generateResValues: Boolean = false,
        val generateBuildConfig: Boolean = false,
    )
}


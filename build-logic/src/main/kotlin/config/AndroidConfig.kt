package config

sealed class AndroidConfig(
    val lintOptions: LintOptions = LintOptions(),
    val variants: Sequence<String> = sequenceOf("debug", "release"),
) {

    data class LintOptions(
        val abortOnError: Boolean = false,
    )

    data class AndroidCommonConfig(
        val targetSdkVersion: Int = 33,
        val compileSdkVersion: Int = 33,
        val minSdkVersion: Int = 24,
        val packagingExcludes: List<String> = emptyList(),
    ) : AndroidConfig()

    data class AndroidAppConfig(
        val id: String,
        val version: Version,
    ) : AndroidConfig() {

        data class Version(
            val code: Int,
            val name: String,
            val postFix: String = code.toString(),
            val postFixSeparator: String = ".",
        ) {

            val formattedName: String
                get() = "$name$postFixSeparator$postFix"
        }
    }

    data class AndroidLibraryConfig(
        val consumerProguardFiles: Sequence<String> = sequenceOf("consumer-rules.pro"),
        val manifestPath: String = "src/androidMain/AndroidManifest.xml",
        val buildFeaturesConfig: BuildFeaturesConfig = BuildFeaturesConfig(),
        val ignoredSourceSets: Sequence<String> = sequenceOf(
            "androidAndroidTestRelease", "androidTestFixtures",
            "androidTestFixturesDebug", "androidTestFixturesRelease",
        ),
    ) : AndroidConfig() {

        data class BuildFeaturesConfig(
            val generateAndroidResources: Boolean = false,
            val generateResValues: Boolean = false,
            val generateBuildConfig: Boolean = false,
        )
    }
}



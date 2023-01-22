package config

sealed class AndroidConfig(
    val lintOptions: LintOptions = LintOptions(),
    val buildTypes: Sequence<AndroidBuildType> = sequenceOf(ReleaseBuildType, DebugBuildType),
) {

    data class LintOptions(
        val abortOnError: Boolean = false,
    )

    interface AndroidBuildType {

        val name: String
        val minify: Boolean
        val shrinkResources: Boolean
        val versionNameSuffix: String?
        val isDebuggable: Boolean
        val multidex: Boolean
    }

    object ReleaseBuildType : AndroidBuildType {

        override val name: String = "release"
        override val minify: Boolean = true
        override val shrinkResources: Boolean = true
        override val versionNameSuffix: String? = null
        override val isDebuggable: Boolean = false
        override val multidex: Boolean = false
    }

    object DebugBuildType : AndroidBuildType {

        override val name: String = "debug"
        override val minify: Boolean = false
        override val shrinkResources: Boolean = false
        override val versionNameSuffix: String = "debug"
        override val isDebuggable: Boolean = true
        override val multidex: Boolean = false
    }

    data class AndroidCommonConfig(
        val targetSdkVersion: Int = 33,
        val compileSdkVersion: Int = 33,
        val minSdkVersion: Int = 24,
        val packagingExcludes: List<String> = emptyList(),
    ) : AndroidConfig()

    data class AndroidAppConfig(
        val id: String,
        val version: Version,
        val splits: Splits? = null,
    ) : AndroidConfig() {

        data class Splits(
            val turnOn: Boolean,
            val universalApk: Boolean,
        )

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



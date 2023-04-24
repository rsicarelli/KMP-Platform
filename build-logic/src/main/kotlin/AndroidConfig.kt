sealed class AndroidConfig(
    val targetSdkVersion: Int = 33,
    val compileSdkVersion: Int = 33,
    val minSdkVersion: Int = 24,
    val packagingExcludes: List<String> = emptyList(),
    val lintOptions: LintOptions = LintOptions(),
    val buildTypes: List<AndroidBuildType> = listOf(ReleaseBuildType, DebugBuildType),
)

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
    val consumerProguardFiles: List<String> = listOf("consumer-rules.pro"),
    val sourceSetName: String = "main",
    val manifestPath: String = "src/androidMain/AndroidManifest.xml",
    val resPath: String = "src/androidMain/res",
    val resourcesPath: String = "src/commonMain/resources",
    val buildFeaturesConfig: BuildFeaturesConfig = BuildFeaturesConfig(),
    val ignoredSourceSets: List<String> = listOf(
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

data class LintOptions(
    val abortOnError: Boolean = false,
)

interface AndroidBuildType {

    val name: String
    val isMinifyEnabled: Boolean
    val shrinkResources: Boolean
    val versionNameSuffix: String?
    val isDebuggable: Boolean
    val multidex: Boolean
}

object ReleaseBuildType : AndroidBuildType {

    override val name: String = "release"
    override val isMinifyEnabled: Boolean = true
    override val shrinkResources: Boolean = true
    override val versionNameSuffix: String? = null
    override val isDebuggable: Boolean = false
    override val multidex: Boolean = false
}

object DebugBuildType : AndroidBuildType {

    override val name: String = "debug"
    override val isMinifyEnabled: Boolean = false
    override val shrinkResources: Boolean = false
    override val versionNameSuffix: String = "debug"
    override val isDebuggable: Boolean = true
    override val multidex: Boolean = false
}



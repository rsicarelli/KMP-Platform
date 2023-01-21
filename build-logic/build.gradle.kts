import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `maven-publish`
    `version-catalog`
}

group = "com.rsicarelli.kmpgradleplatform"
version = libs.versions.kmpGradlePlatform.get()

dependencies {
    compileOnly(libs.gradlePlugin.kotlin)
    compileOnly(libs.gradlePlugin.android)
    compileOnly(libs.gradlePlugin.compose)
    compileOnly(libs.gradlePlugin.detekt)
}

catalog {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))
    }
}

gradlePlugin {
    with(plugins) {
        register("KMPGradlePlugin") {
            id = "com.rsicarelli.kmp-gradle-platform"
            version = libs.versions.kmpGradlePlatform.get()
            implementationClass = "KMPGradlePlatformPlugin"
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("versionCatalog") {
            artifactId = "versioncatalog"
            version = libs.versions.kmpGradlePlatform.get()
            from(components["versionCatalog"])
        }
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        allWarningsAsErrors = false
        freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.RequiresOptIn")
        jvmTarget = "11"
    }
}

extensions.configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
